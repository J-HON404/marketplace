package com.unicam.cs.progettoweb.marketplace.repository;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Id(Long userId);
    boolean existsByUser_Id(Long userId);
    Optional<Cart> findByUser_IdAndShop_Id(Long profileId, Long shopId);
    List<Cart> findByShop_Id(Long shopId);
    void deleteByShop_Id(Long shopId);
}