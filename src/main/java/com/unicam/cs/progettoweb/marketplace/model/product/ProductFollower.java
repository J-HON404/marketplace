package com.unicam.cs.progettoweb.marketplace.model.product;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "product_followers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "user_id"}))
//lo stesso user non può può seguire lo stesso prodotto due volte contempo.
public class ProductFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Profile user;
}