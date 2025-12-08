package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    // controlla se esiste un prodotto con quel nome nello stesso shop
    boolean existsByNameAndShop(String name, Shop shop);
    //prodotti disponibili e non disponibili di uno shop
    List<Product> findByShop_Id(Long shopId);
    // prodotti disponibili per uno shop specifico
    List<Product> findByShopAndAvailabilityDateLessThanEqual(Shop shop, LocalDate date);
    // tutti i prodotti degli shop già disponibili (availabilityDate <= oggi)
    List<Product> findByAvailabilityDateLessThanEqual(LocalDate date);
    // trova un prdotti da uno shop
    List<Product> findByShopId(Long shopId);
}
