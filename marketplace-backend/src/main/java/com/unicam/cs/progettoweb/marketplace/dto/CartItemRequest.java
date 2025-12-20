package com.unicam.cs.progettoweb.marketplace.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}