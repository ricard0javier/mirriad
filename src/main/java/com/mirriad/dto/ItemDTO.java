package com.mirriad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Item DTO definition, it contains all the exchangeable information of a set of equals items
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {

    /**
     * Unique identifier
     */
    private long id;

    /**
     * Name of the product
     */
    private String name;

    /**
     * Quantity of the same product
     */
    private int quantity;

    /**
     * Price of each product
     */
    private float unitPrice;

    /**
     * Discount applied to the instance
     */
    private float discount;

}
