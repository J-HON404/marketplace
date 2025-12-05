package com.unicam.cs.progettoweb.marketplace.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shops")
@Data
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "seller_id") //fk verso seller_id
    private Seller seller;
}
