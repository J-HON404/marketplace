package com.unicam.cs.progettoweb.marketplace.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) //fk verso cutomer_id
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false) //fk verso shop_id
    private Shop shop;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private Double total;
}