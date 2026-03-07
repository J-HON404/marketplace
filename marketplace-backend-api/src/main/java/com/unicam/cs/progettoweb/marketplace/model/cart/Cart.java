package com.unicam.cs.progettoweb.marketplace.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il carrello di un utente per un determinato negozio.
 * Contiene la lista di articoli presenti e i riferimenti all'utente e al negozio.
 */

@Data
@Entity
@NoArgsConstructor
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) //fk verso user_id
    @JsonIgnore
    private Profile user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) //cartItem ha riferimento con FK su Cart
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnore
    private Shop shop; //fk verso shop_id

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }


}