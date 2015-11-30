package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import com.mirriad.service.rule.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Order(200)
@Slf4j
public class GetTwoForLessPriceRuleImpl implements PriceRule {

    public static final int NUMBER_OF_ITEMS_PER_RULE = 2;
    private float discountRate;

    @Autowired
    public GetTwoForLessPriceRuleImpl(@Value("${rules.getTwoForLess.discountRate}") float discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Set<Item> applyRule(Set<Item> items) {

        // apply only to items without discount
        items.stream()
                .filter(Item::isAvailableForDiscount)
                .filter(item -> item.getQuantityAvailableForDiscount() >= NUMBER_OF_ITEMS_PER_RULE)
                .forEach(item -> {

                    int quantityFree = item.getQuantityAvailableForDiscount() / NUMBER_OF_ITEMS_PER_RULE;
                    int productsInOffer = quantityFree * NUMBER_OF_ITEMS_PER_RULE;
                    float discount = discountRate * item.getUnitPrice() * quantityFree;

                    item.applyDiscount(productsInOffer, discount);

                    log.info("'{}' item(s) '{}' received a discount of '{}'", productsInOffer, item.getId(), item.getDiscount());

                });

        return items;

    }
}
