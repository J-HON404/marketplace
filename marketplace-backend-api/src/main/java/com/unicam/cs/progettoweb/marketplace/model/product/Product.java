package com.unicam.cs.progettoweb.marketplace.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un prodotto all'interno di un negozio.
 * Contiene informazioni sul nome, descrizione, prezzo, quantità, disponibilità e notifiche associate.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "name"}))
          //lo stesso shop non può avere due prodotti con stesso name
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false) // fk verso shop_id
    @JsonIgnore
    private Shop shop;

    @Column(nullable = false)
    private LocalDate availabilityDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProductNotice> notices = new ArrayList<>();
}
