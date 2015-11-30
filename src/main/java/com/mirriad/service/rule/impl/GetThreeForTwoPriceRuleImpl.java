package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import com.mirriad.service.rule.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Order(100)
@Slf4j
public class GetThreeForTwoPriceRuleImpl implements PriceRule {

    public static final int NUMBER_OF_ITEMS_PER_RULE = 3;

    @Override
    public Set<Item> applyRule(Set<Item> items) {

        // apply only to items without discount
        items.stream()
                .filter(Item::isAvailableForDiscount)
                .filter(item -> item.getQuantityAvailableForDiscount() >= NUMBER_OF_ITEMS_PER_RULE)
                .forEach(item -> {

                    int quantityFree = item.getQuantityAvailableForDiscount() / NUMBER_OF_ITEMS_PER_RULE;
                    int productsInOffer = quantityFree * NUMBER_OF_ITEMS_PER_RULE;
                    float discount = item.getUnitPrice() * quantityFree;

                    item.applyDiscount(productsInOffer, discount);

                    log.info("'{}' item(s) '{}' received a discount of '{}'", productsInOffer, item.getId(), item.getDiscount());

                });

        return items;

    }
}
