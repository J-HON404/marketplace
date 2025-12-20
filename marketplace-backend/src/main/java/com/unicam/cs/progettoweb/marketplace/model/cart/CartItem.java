package com.unicam.cs.progettoweb.marketplace.model.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) //fk verso cart_id
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)//fk verso prodotto_id
    private Product product;

    @Column(nullable = false)
    private int quantity;

}
