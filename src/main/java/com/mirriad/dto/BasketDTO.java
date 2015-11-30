package com.mirriad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Representation of the basket, initially it contains a list of items
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketDTO {

    /**
     * List of items to buy
     */
    private List<ItemDTO> items;
}
