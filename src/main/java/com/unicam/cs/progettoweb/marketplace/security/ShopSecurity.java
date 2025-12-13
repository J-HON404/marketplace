package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopService;
import org.springframework.stereotype.Component;

@Component
public class ShopSecurity {

    private final ShopService shopService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductNoticeService productNoticeService;

    public ShopSecurity(ShopService shopService, OrderService orderService, ProductService productService, ProductNoticeService productNoticeService) {
        this.shopService = shopService;
        this.orderService = orderService;
        this.productService = productService;
        this.productNoticeService = productNoticeService;
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

    public boolean isSellerOfProductForNotice(Long sellerId, Long noticeId) {
        ProductNotice notice = productNoticeService.getProductNoticeById(noticeId);
        return notice.getProduct().getShop().getSeller().getId().equals(sellerId);
    }

}
