package com.mirriad;

import com.mirriad.dto.BasketDTO;
import com.mirriad.dto.ItemDTO;
import com.mirriad.dto.ReceiptDTO;
import com.mirriad.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Integration test of the application
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ApplicationIT {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {

        this.base = new URL("http://localhost:" + port + "/");

        template = new TestRestTemplate();
    }

    @Test
    public void testCheckout() throws Exception {

        ItemDTO itemA = new ItemDTO();
        itemA.setId(100);
        itemA.setUnitPrice(1.25f);
        itemA.setQuantity(7);

        ItemDTO itemB = new ItemDTO();
        itemB.setId(200);
        itemB.setUnitPrice(0.17f);
        itemB.setQuantity(2);

        List<ItemDTO> items = new ArrayList<>();
        items.add(itemA);
        items.add(itemB);

        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setItems(items);

        String url = base.toString() + "checkout";

        ReceiptDTO receiptDTO = template.postForObject(url, basketDTO, ReceiptDTO.class);

        assertEquals(6.5475, receiptDTO.getGrandTotal(), 0.005);
        assertEquals(2, receiptDTO.getItems().size());

    }

}
