package com.mirriad.controller;

import com.mirriad.model.Item;
import com.mirriad.service.offer.OfferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test suite for the cases present on {@link CheckoutController}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class CheckoutControllerTest {

    /**
     * Test servlet Context
     */
    private MockMvc mockMvc;

    /**
     * Mock of the offerService used in the controller
     */
    @Mock
    private OfferService offerServiceMock;

    /**
     * Configures the mock annotations and register the CheckoutController in the servlet dispatcher
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        CheckoutController checkoutController = new CheckoutController(offerServiceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();

    }

    /**
     * Test that the controller is up and working properly integrated with the spring framework.
     * At the same time it check that the happy path of receiving a JSON content, processing it and returning the
     * receipt information
     * @throws Exception
     */
    @Test
    public void testCheckoutController() throws Exception {

        // mock the offerService response
        Item item = new Item(1, 10, 1);
        item.setName("Apple");

        Set<Item> items = new HashSet<>();
        items.add(item);

        when(offerServiceMock.calculatePrices(any())).thenReturn(items);

        // perform a POST call to the controller and check the behaviour
        mockMvc.perform(
                post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"items\":[{\"id\":1,\"name\":\"apple\",\"unitPrice\":10, \"quantity\":1}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grandTotal", equalTo(10.0)))
                .andExpect(jsonPath("$.items.length()", equalTo(1)))
                .andExpect(jsonPath("$.items[0].id", equalTo(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Apple")))
                .andExpect(jsonPath("$.items[0].quantity", equalTo(1)))
                .andExpect(jsonPath("$.items[0].unitPrice", equalTo(10.0)));

    }
}