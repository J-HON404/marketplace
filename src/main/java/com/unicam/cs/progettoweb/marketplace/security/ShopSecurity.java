package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.stereotype.Component;

@Component
public class ShopSecurity {

    private final SellerShopService shopService;
    private final OrderService orderService;
    private final ProductService productService;

    public ShopSecurity(SellerShopService shopService, OrderService orderService, ProductService productService) {
        this.shopService = shopService;
        this.orderService = orderService;
        this.productService = productService;
    }

    public boolean isSellerOfShop(Long sellerId, Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return shop.getSeller().getId().equals(sellerId);
    }

    public boolean isSellerOfOrder(Long sellerId, Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order.getShop().getSeller().getId().equals(sellerId);
    }

    public boolean isSellerOfProduct(Long sellerId, Long productId) {
        Product product = productService.getProductById(productId);
        return product.getShop().getSeller().getId().equals(sellerId);
    }

}
