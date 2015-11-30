package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test suite for the class {@link GetTwoForLessPriceRuleImpl}
 */
public class GetTwoForLessPriceRuleImplTest {

    public static final long ITEM_ID = 1L;
    public static final String ITEM_NAME = "Apple";
    public static final float ITEM_UNIT_PRICE = 10f;
    public static final int ITEM_QUANTITY = 2;
    public static final float DISCOUNT_RATE = 0.5F;
    private GetTwoForLessPriceRuleImpl target;

    /**
     * Configures the target
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        target = new GetTwoForLessPriceRuleImpl(DISCOUNT_RATE);

    }

    /**
     * Test that rule is applied correctly
     *
     * @throws Exception
     */
    @Test
    public void testRuleIsApplied() throws Exception {

        Item item = new Item(ITEM_ID, ITEM_UNIT_PRICE, ITEM_QUANTITY);
        item.setName(ITEM_NAME);

        Set<Item> items = new HashSet<>();
        items.add(item);

        Set<Item> newItems = target.applyRule(items);

        assertNotNull(newItems);
        assertEquals(1, newItems.size());

        Item actual = (Item) newItems.toArray()[0];
        assertEquals(ITEM_ID, actual.getId());
        assertEquals(ITEM_NAME, actual.getName());
        assertEquals(ITEM_UNIT_PRICE, actual.getUnitPrice(), 0.01);
        assertEquals(ITEM_QUANTITY, actual.getQuantity());
        assertEquals(ITEM_UNIT_PRICE * DISCOUNT_RATE, actual.getDiscount(), 0.01);
        assertFalse(actual.isAvailableForDiscount());

    }

    /**
     * Test that rule is not applied
     *
     * @throws Exception
     */
    @Test
    public void testRuleIsNotApplied() throws Exception {

        // mock the offerService response
        Item item = new Item(ITEM_ID, ITEM_UNIT_PRICE, 1);
        item.setName(ITEM_NAME);

        Set<Item> items = new HashSet<>();
        items.add(item);

        Set<Item> newItems = target.applyRule(items);

        assertNotNull(newItems);
        assertEquals(1, newItems.size());

        Item actual = (Item) newItems.toArray()[0];
        assertEquals(ITEM_ID, actual.getId());
        assertEquals(ITEM_NAME, actual.getName());
        assertEquals(ITEM_UNIT_PRICE, actual.getUnitPrice(), 0.01);
        assertEquals(1, actual.getQuantity());
        assertEquals(0, actual.getDiscount(), 0.01);
        assertTrue(actual.isAvailableForDiscount());

    }
}