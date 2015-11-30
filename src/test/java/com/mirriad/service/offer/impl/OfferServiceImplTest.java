package com.mirriad.service.offer.impl;

import com.mirriad.model.Item;
import com.mirriad.service.rule.PriceRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test suit for {@link OfferServiceImplTest}
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferServiceImplTest {

    /**
     * Class under test
     */
    private OfferServiceImpl target;

    /**
     * Mock of the price rule service
     */
    @Mock
    private PriceRule priceRuleMock;

    /**
     * Configures the offer service with a single price rule mock
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        List<PriceRule> priceRules = new ArrayList<>();
        priceRules.add(priceRuleMock);

        target = new OfferServiceImpl(priceRules);
    }

    @Test
    public void testCalculatePrices() throws Exception {

        // mock the offerService response
        Item item = new Item(1, 10, 1);
        item.setName("Apple");

        Set<Item> items = new HashSet<>();
        items.add(item);

        when(priceRuleMock.applyRule(items)).thenReturn(items);

        Set<Item> newItems = target.calculatePrices(items);

        // verify that the rule is applied and response size is the same
        verify(priceRuleMock).applyRule(items);
        assertNotNull(newItems);
        assertEquals(1, newItems.size());

    }
}