package com.unicam.cs.progettoweb.marketplace.dto;

import lombok.Data;

/**
 * DTO per le richieste relative agli articoli del carrello.
 */

@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}