package com.mirriad.service.offer.impl;

import com.mirriad.model.Item;
import com.mirriad.service.offer.OfferService;
import com.mirriad.service.rule.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation of the {@link OfferService} interface
 */
@Service
@Slf4j
public class OfferServiceImpl implements OfferService {

    /**
     * List of rules to be applied
     */
    private List<PriceRule> priceRules;

    /**
     * Constructor that inject the list of rules
     * @param priceRules List of rules to apply
     */
    @Autowired
    public OfferServiceImpl(List<PriceRule> priceRules) {

        this.priceRules = priceRules;

    }

    @Override
    public Set<Item> calculatePrices(Set<Item> originalItems) {

        // validating parameters
        if (originalItems == null || originalItems.isEmpty()) {

            log.debug("List of items is empty, nothing to do");

            return originalItems;

        }

        // validating rules
        if (priceRules == null || priceRules.isEmpty()) {

            log.debug("List of rules is empty, nothing to do");

            return originalItems;

        }

        // apply rules
        log.debug("Calculating the discounts applying '{}' rules", priceRules.size());

        Set<Item> items = new HashSet<>(originalItems);

        for (PriceRule priceRule : priceRules) {

            log.debug("Applying rule '{}'", priceRule.getClass().getName());

            items = priceRule.applyRule(items);

        }

        return items;
    }
}
