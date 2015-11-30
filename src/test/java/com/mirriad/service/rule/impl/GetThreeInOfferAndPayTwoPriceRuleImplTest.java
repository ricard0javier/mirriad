package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test suit for the class {@link GetThreeInOfferAndPayTwoPriceRuleImpl}
 */
public class GetThreeInOfferAndPayTwoPriceRuleImplTest {

    public static final int ITEM_A_QUANTITY = 1;
    public static final int ITEM_B_QUANTITY = 1;
    public static final int ITEM_C_QUANTITY = 1;
    public static final long ITEM_A_ID = 100;
    public static final long ITEM_B_ID = 200;
    public static final long ITEM_C_ID = 300;
    public static final float ITEM_A_UNIT_PRICE = 10;
    public static final float ITEM_B_UNIT_PRICE = 2;
    public static final float ITEM_C_UNIT_PRICE = 4;

    private GetThreeInOfferAndPayTwoPriceRuleImpl target;

    @Before
    public void setUp() throws Exception {

        target = new GetThreeInOfferAndPayTwoPriceRuleImpl(ITEM_A_ID, ITEM_B_ID, ITEM_C_ID);

    }

    @Test
    public void testRuleIsApplied() throws Exception {

        Item mangoItem = new Item(ITEM_A_ID, ITEM_A_UNIT_PRICE, ITEM_A_QUANTITY);

        Item bananaItem = new Item(ITEM_B_ID, ITEM_B_UNIT_PRICE, ITEM_B_QUANTITY);

        Item appleItem = new Item(ITEM_C_ID, ITEM_C_UNIT_PRICE, ITEM_C_QUANTITY);

        Set<Item> items = new HashSet<>();
        items.add(mangoItem);
        items.add(bananaItem);
        items.add(appleItem);

        target.applyRule(items);

        assertFalse(mangoItem.isAvailableForDiscount());
        assertEquals(0, mangoItem.getDiscount(), 0.0001);

        assertFalse(bananaItem.isAvailableForDiscount());
        assertEquals(ITEM_B_UNIT_PRICE, bananaItem.getDiscount(), 0.0001);

        assertFalse(appleItem.isAvailableForDiscount());
        assertEquals(0, appleItem.getDiscount(), 0.0001);

    }

    @Test
    public void testRuleIsNotApplied() throws Exception {

        Item mangoItem = new Item(ITEM_A_ID, ITEM_A_UNIT_PRICE, ITEM_A_QUANTITY);

        Item bananaItem = new Item(ITEM_B_ID, ITEM_B_UNIT_PRICE, ITEM_B_QUANTITY);

        Set<Item> items = new HashSet<>();
        items.add(mangoItem);
        items.add(bananaItem);

        target.applyRule(items);

        assertEquals(0, mangoItem.getDiscount(), 0.0001);
        assertTrue(mangoItem.isAvailableForDiscount());

        assertEquals(0, bananaItem.getDiscount(), 0.0001);
        assertTrue(bananaItem.isAvailableForDiscount());
    }
}