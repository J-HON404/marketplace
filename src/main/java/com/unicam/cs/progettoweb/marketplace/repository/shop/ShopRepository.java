package com.unicam.cs.progettoweb.marketplace.repository.shop;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByName(String name);
    Optional<Shop> findById(Long id);
    Optional<Shop> findBySeller_Id(Long profileId);
    boolean existsBySeller_Id(Long profileId);
}
