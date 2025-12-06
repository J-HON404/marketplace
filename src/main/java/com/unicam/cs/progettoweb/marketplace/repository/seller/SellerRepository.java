package com.unicam.cs.progettoweb.marketplace.repository.seller;

import com.unicam.cs.progettoweb.marketplace.model.Seller;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByShopId(Long shop_id);
}
