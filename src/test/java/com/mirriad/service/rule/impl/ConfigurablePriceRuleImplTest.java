package com.mirriad.service.rule.impl;

import com.mirriad.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurablePriceRuleImplTest {

    public static final int ITEM_A_QUANTITY = 5;
    public static final int ITEM_B_QUANTITY = 2;
    public static final long ITEM_A_ID = 100L;
    public static final long ITEM_B_ID = 200L;
    public static final float ITEM_A_UNIT_PRICE = 10F;
    public static final float ITEM_B_UNIT_PRICE = 2F;
    private ConfigurablePriceRuleImpl target;

    @Before
    public void setUp() throws Exception {

        target = new ConfigurablePriceRuleImpl(5, 100, 2, 200);

    }

    @Test
    public void testRuleIsApplied() throws Exception {

        Item mangoItem = new Item(ITEM_A_ID, ITEM_A_UNIT_PRICE, ITEM_A_QUANTITY);

        Item bananaItem = new Item(ITEM_B_ID, ITEM_B_UNIT_PRICE, ITEM_B_QUANTITY);

        Set<Item> items = new HashSet<>();
        items.add(mangoItem);
        items.add(bananaItem);

        items = target.applyRule(items);

        assertNotNull(items);
        assertEquals(2, items.size());

        for (Item item : items) {

            if (item.getId() == ITEM_A_ID) {
                assertEquals(ITEM_A_QUANTITY, item.getQuantity());
                assertEquals(ITEM_A_UNIT_PRICE, item.getUnitPrice(), 0.01);
                assertEquals(0, item.getDiscount(), 0.01);
            }
            if (item.getId() == ITEM_B_ID) {
                assertEquals(ITEM_B_QUANTITY, item.getQuantity());
                assertEquals(ITEM_B_UNIT_PRICE, item.getUnitPrice(), 0.01);
                assertEquals(4, item.getDiscount(), 0.01);

            }
        }

    }

    @Test
    public void testRuleIsNotApplied() throws Exception {

        Item mangoItem = new Item(ITEM_A_ID, ITEM_A_UNIT_PRICE, 2);

        Item bananaItem = new Item(ITEM_B_ID, ITEM_B_UNIT_PRICE, ITEM_B_QUANTITY);

        Set<Item> items = new HashSet<>();
        items.add(mangoItem);
        items.add(bananaItem);

        items = target.applyRule(items);

        assertNotNull(items);
        assertEquals(2, items.size());

        for (Item item : items) {

            if (item.getId() == ITEM_A_ID) {
                assertEquals(2, item.getQuantity());
                assertEquals(ITEM_A_UNIT_PRICE, item.getUnitPrice(), 0.01);
                assertEquals(0, item.getDiscount(), 0.01);
            }
            if (item.getId() == ITEM_B_ID) {
                assertEquals(ITEM_B_QUANTITY, item.getQuantity());
                assertEquals(ITEM_B_UNIT_PRICE, item.getUnitPrice(), 0.01);
                assertEquals(0, item.getDiscount(), 0.01);

            }
        }

    }
}