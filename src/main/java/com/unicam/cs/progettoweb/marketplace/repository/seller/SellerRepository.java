package com.unicam.cs.progettoweb.marketplace.repository.seller;

import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByShop_Id(Long shop_id);
}
