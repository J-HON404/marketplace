package com.unicam.cs.progettoweb.marketplace.repository.cart;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
