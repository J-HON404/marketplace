package com.unicam.cs.progettoweb.marketplace.repository;

import com.unicam.cs.progettoweb.marketplace.model.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    // controlla se esiste un prodotto con quel nome nello stesso shop
    boolean existsByNameAndShop(String name, Shop shop);
    //prodotti disponibili e non disponibili di uno shop
    List<Product> findByShop(Shop shop);
    // prodotti disponibili per uno shop specifico
    List<Product> findByShopAndAvailabilityDateLessThanEqual(Shop shop, LocalDate date);
    // Tutti i prodotti degli shop già disponibili (availabilityDate <= oggi)
    List<Product> findByAvailabilityDateLessThanEqual(LocalDate date);
}
