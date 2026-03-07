package com.unicam.cs.progettoweb.marketplace.repository;

import com.unicam.cs.progettoweb.marketplace.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository JPA per la gestione dei negozi.
 */

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByName(String name);
    Optional<Shop> findById(Long id);
    Optional<Shop> findBySeller_Id(Long profileId);
    boolean existsBySeller_Id(Long profileId);
    boolean existsByIdAndSeller_Id(Long shopId, Long sellerId);
}
