package com.unicam.cs.progettoweb.marketplace.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicam.cs.progettoweb.marketplace.model.profile.Profile;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

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
    private List<CartItem> items = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnore
    private Shop shop; //fk verso shop_id

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }


}