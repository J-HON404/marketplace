package com.unicam.cs.progettoweb.marketplace.repository.shop;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByName(String name);
    Optional<Shop> findById(Long id);
    List<Shop> findBySeller_Id(Long sellerId);
}
