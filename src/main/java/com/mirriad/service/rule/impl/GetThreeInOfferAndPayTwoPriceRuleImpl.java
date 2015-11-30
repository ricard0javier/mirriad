package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import com.mirriad.service.rule.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Order(300)
@Slf4j
public class GetThreeInOfferAndPayTwoPriceRuleImpl implements PriceRule {

    private List<Item> offerItems;

    @Autowired
    public GetThreeInOfferAndPayTwoPriceRuleImpl(
            @Value("${rules.getThreeInOfferForTwo}") long... offerItemIds) {

        log.debug("Building '{}' rule. The items '{}' are in offer", this.getClass().getName(), offerItemIds);

        this.offerItems = new ArrayList<>();

        for (long offerItemId : offerItemIds) {

            Item offerItem = new Item(offerItemId, 0, 0);

            this.offerItems.add(offerItem);
        }

    }

    @Override
    public Set<Item> applyRule(Set<Item> items) {

        int itemsInOffer = 0;
        Item cheapestItem = (Item) items.toArray()[0];

        Set<Item> candidates = new HashSet<>();

        // count the number of item bought that are in the offer
        for (Item item : items) {

            // apply only to items without discount and match itemId in offer
            if (!item.isAvailableForDiscount() || !offerItems.contains(item)) {
                continue;
            }

            log.trace("The item '{}' is a candidate for the rule", item.getId());
            candidates.add(item);
            itemsInOffer += item.getQuantityAvailableForDiscount();

            if (item.getUnitPrice() <= cheapestItem.getUnitPrice()) {
                cheapestItem = item;
            }

        }

        if (itemsInOffer < 3) {

            log.info("There are not enough candidates to apply the rule");

            return items;

        }

        int quantityForFree = itemsInOffer / 3;
        float discount = cheapestItem.getUnitPrice() * quantityForFree;

        for (Item candidate : candidates) {

            // apply discount to the cheapest product a decrease the discount availability of the other
            if (candidate.equals(cheapestItem)) {

                cheapestItem.applyDiscount(quantityForFree, discount);

                log.info("'{}' item(s) '{}' received a discount of '{}'", quantityForFree, cheapestItem.getId(), discount);

            }
            else {

                int quantityOfProductA = quantityForFree * 3;
                candidate.applyDiscount(quantityOfProductA, 0);
                log.info("'{}' item(s) '{}' participated in the discount of '{}'", quantityOfProductA, candidate.getId(), cheapestItem.getId());

            }

        }

        return items;

    }
}
