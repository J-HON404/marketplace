package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // controlla se esiste un prodotto con quel nome nello stesso shop
    boolean existsByNameAndShop(String name, Shop shop);
    // tutti i prodotti di uno shop (disponibili e non disponibili)
    List<Product> findByShop_Id(Long shopId);
    // prodotti già disponibili (availabilityDate <= oggi) per uno shop specifico
    List<Product> findByShopAndAvailabilityDateLessThanEqual(Shop shop, LocalDate date);
    // prodotti di uno shop con quantità esattamente uguale a un certo numero
    List<Product> findByShopAndQuantityEquals(Shop shop, int quantity);
    // prodotti di uno shop con quantità = 0 (non disponibili)
    default List<Product> findUnavailableProductsByShop(Shop shop) {
        return findByShopAndQuantityEquals(shop, 0);
    }
    // prodotti di uno shop con disponibilità futura (availabilityDate > oggi)
    List<Product> findByShopAndAvailabilityDateGreaterThan(Shop shop, LocalDate date);
    boolean existsByIdAndShop_Seller_Id(Long productId, Long sellerId);
    boolean existsByName(String name);
}
