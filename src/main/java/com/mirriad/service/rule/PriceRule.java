package com.mirriad.service.rule;

import com.mirriad.model.Item;

import java.util.Set;

/**
 * Interface to be implemented by all the rules to be applied in other to calculate the discount of the products
 */
public interface PriceRule {

    /**
     * Returns a Set of items after applying the offer logic to the set receive as parameter.
     * The parameter should be a mutable list of items, and the offer will be applied only to those
     * items that hasn't been processed before in other rule
     * @param items Mutable set of items
     * @return Update set of items after calculating the offers to apply
     */
    Set<Item> applyRule(Set<Item> items);

}
