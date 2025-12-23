package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) //permette lettura atomica di un prodotto, in situazioni di acquisto concorrenti
    Optional<Product> findById(Long id);
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
    // Controlla se un prodotto con productId appartiene allo shop con shopId
    boolean existsByIdAndShop_Id(Long productId, Long shopId);
}
