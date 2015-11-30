package com.mirriad.service.offer;


import com.mirriad.model.Item;

import java.util.Set;

/**
 * Provides all the functionality needed to calculate the discounts
 */
public interface OfferService {

    /**
     * Returns a Set of Items after applying a set of rules
     * @param originalItems
     * @return
     */
    Set<Item> calculatePrices(Set<Item> originalItems);

}
