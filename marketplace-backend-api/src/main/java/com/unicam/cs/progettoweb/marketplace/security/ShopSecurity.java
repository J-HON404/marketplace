package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.repository.OrderRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.repository.ShopRepository;
import org.springframework.stereotype.Component;

/**
 * Componente di sicurezza per operazioni legate agli shop.
 * Fornisce metodi per verificare se un seller è proprietario di uno shop, ordine o prodotto.
 */

@Component("shopSecurity")
public class ShopSecurity {

    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ShopSecurity(ShopRepository shopRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.shopRepository = shopRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public boolean isSellerOfShop(Long sellerId, Long shopId) {
        return shopRepository.existsByIdAndSeller_Id(shopId, sellerId);
    }

    public boolean isSellerOfOrder(Long sellerId, Long orderId) {
        return orderRepository.existsByIdAndShop_Seller_Id(orderId, sellerId);
    }

    public boolean isSellerOfProduct(Long sellerId, Long productId) {
        return productRepository.existsByIdAndShop_Seller_Id(productId, sellerId);
    }

    public boolean isProductBelongsToShop(Long shopId,Long productId){
        return productRepository.existsByIdAndShop_Id(productId,shopId);
    }
}
