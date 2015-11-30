package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import com.mirriad.service.rule.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@Order(400)
public class ConfigurablePriceRuleImpl implements PriceRule {

    private int quantityRequired;
    private long requiredItemId;
    private int multiplier;
    private long freeItemId;

    @Autowired
    public ConfigurablePriceRuleImpl(
            @Value("${rules.configurable.n}") int quantityRequired,
            @Value("${rules.configurable.x}") long requiredItemId,
            @Value("${rules.configurable.k}") int multiplier,
            @Value("${rules.configurable.y}") long freeItemId) {

        this.quantityRequired = quantityRequired;
        this.requiredItemId = requiredItemId;
        this.multiplier = multiplier;
        this.freeItemId = freeItemId;

        log.debug("Building the configurable offer, quantityRequired='{}', requiredItemId='{}', multiplier='{}', freeItemId='{}'",
                quantityRequired, requiredItemId, multiplier, freeItemId);
    }

    @Override
    public Set<Item> applyRule(Set<Item> items) {

        int quantityFree = 0;

        for (Item item : items) {

            // get the number of items that match the rule
            if (requiredItemId == item.getId() && item.isAvailableForDiscount()) {

                quantityFree = multiplier * (item.getQuantityAvailableForDiscount() / quantityRequired);

                // as this rule affects other product the amount to discount is zero then
                item.applyDiscount((item.getQuantityAvailableForDiscount() % quantityRequired), 0);

                log.info("Product '{}' matches the offer, '{}' product(s) of '{}' in the basket would be for free",
                        item.getId(), quantityFree, freeItemId);

                break;

            }

        }

        if (quantityFree == 0) {

            log.debug("The basket didn't match the 'Configurable' offer");

            return items;

        }

        // apply discount
        for (Item item : items) {

            if (freeItemId != item.getId()) {
                continue;
            }

            float discount = item.getQuantityAvailableForDiscount() * item.getUnitPrice();
            item.applyDiscount(quantityFree, discount);

        }


        return items;

    }
}
