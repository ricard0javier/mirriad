package com.mirriad.dto;

import lombok.Data;

import java.util.Set;

/**
 * Gathers the information of the processed items
 */
@Data
public class ReceiptDTO {

    /**
     * Total amount of the items after applying the discounts
     */
    private Float grandTotal;

    /**
     * Details of the sold item
     */
    private Set<ItemDTO> items;

}
