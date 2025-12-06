package com.unicam.cs.progettoweb.marketplace.repository.shop;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findShopByName(Shop name);
    Optional<Shop> findShopById(Shop id);
}