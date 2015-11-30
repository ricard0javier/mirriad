package com.mirriad.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model representation of an item
 */
@Data
@EqualsAndHashCode(of = "id")
public class Item {

    /**
     * Unique identifier of the item
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
     * Discount applied to all the products of this instance
     */
    private float discount;

    /**
     * Price of each product
     */
    private float unitPrice;

    /**
     * Flag if the offers has been calculated
     */
    private int quantityAvailableForDiscount;

    /**
     * default constructor
     */
    public Item() {
    }

    /**
     * Basic constructor
     * @param id identifier del product
     * @param unitPrice price per element
     * @param quantity number of elements
     */
    public Item(long id, float unitPrice, int quantity) {

        this.id = id;
        this.unitPrice = unitPrice;

        this.quantity = quantity;
        this.quantityAvailableForDiscount = quantity;
    }

    /**
     * Returns true if there are elements available for discounts
     * @return boolean
     */
    public boolean isAvailableForDiscount() {

        return quantityAvailableForDiscount > 0;

    }

    /**
     * Reduces the quantity of elements available for a discount
     * @param quantity number of elements to apply discount
     * @param amount value to discount
     */
    public void applyDiscount(int quantity, float amount) {

        quantityAvailableForDiscount -= quantity;
        discount += amount;

        if (quantityAvailableForDiscount < 0) {
            quantityAvailableForDiscount = 0;
        }

        float totalItem = unitPrice * this.quantity;
        if (discount > totalItem) {
            discount = totalItem;
        }

    }



}
