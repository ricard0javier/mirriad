package com.mirriad.controller;

import com.mirriad.dto.BasketDTO;
import com.mirriad.dto.ItemDTO;
import com.mirriad.dto.ReceiptDTO;
import com.mirriad.model.Item;
import com.mirriad.service.offer.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Process all the request made to the "/checkout" endpoint
 */
@Slf4j
@RestController
public class CheckoutController {

    /**
     * Service used to process the offers
     */
    private OfferService offerService;

    /**
     * Bean Mapper to convert objects between layers
     */
    private Mapper mapper = new DozerBeanMapper();

    /**
     * Constructor used to inject dependencies
     * @param offerService Service used to process the offers
     */
    @Autowired
    public CheckoutController(OfferService offerService) {

        this.offerService = offerService;

    }

    /**
     * Receives all the POST requests made to the "/checkout" endpoint and return the receipt information
     * @param basketDTO Represents the Body of the request
     * @return A receipt DTO
     */
    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ReceiptDTO checkout(@RequestBody BasketDTO basketDTO) {

        log.debug("Checkout started");

        // Validate parameters
        if (basketDTO == null || basketDTO.getItems() == null) {

            log.warn("Trying to checkout an empty basket");
            throw new IllegalArgumentException("An empty basket can not bet checked");

        }

        // calculate the prices
        log.trace("Calculating the prices");
        Set<Item> originalItems = mapFromDTOs(basketDTO.getItems());

        Set<Item> processedItems = offerService.calculatePrices(originalItems);

        // iterate over the items processed to get the information for the receipt
        Set<ItemDTO> processedItemDTOs = new HashSet<>();
        float grandTotal = 0;

        for (Item item : processedItems) {

            ItemDTO itemDTO = mapper.map(item, ItemDTO.class);

            processedItemDTOs.add(itemDTO);

            grandTotal += (item.getQuantity() * item.getUnitPrice()) - item.getDiscount();

        }

        // Build the response
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setGrandTotal(grandTotal);
        receiptDTO.setItems(processedItemDTOs);

        log.info("Checkout completed, grand total '{}' for '{}' items", grandTotal, processedItems.size());

        return receiptDTO;
    }

    /**
     * Maps from a list of ItemDTOs to a set of Items
     * @param itemDTOs DTOs to map
     * @return Model representation of the list
     */
    private Set<Item> mapFromDTOs(List<ItemDTO> itemDTOs) {

        Map<Long, Item> mappedItems = new HashMap<>();

        for (ItemDTO itemDTO : itemDTOs) {

            // increase the quantity of the item
            Item oldItem = mappedItems.get(itemDTO.getId());

            if (oldItem == null) {

                Item item = mapper.map(itemDTO, Item.class);
                item.setQuantityAvailableForDiscount(itemDTO.getQuantity());
                mappedItems.put(itemDTO.getId(), item);

            }
            else {

                oldItem.setQuantity(oldItem.getQuantity() + itemDTO.getQuantity());

            }

        }

        return new HashSet<>(mappedItems.values());
    }
}
