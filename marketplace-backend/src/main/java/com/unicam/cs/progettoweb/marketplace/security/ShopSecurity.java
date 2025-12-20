package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.repository.OrderRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.repository.ShopRepository;
import org.springframework.stereotype.Component;

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
}
