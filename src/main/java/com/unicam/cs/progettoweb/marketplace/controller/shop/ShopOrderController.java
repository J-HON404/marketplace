package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shops/{shopId}/orders")
public class ShopOrderController {

    private final ShopOrderService shopOrderService;

    public ShopOrderController(ShopOrderService shopOrderService) {
        this.shopOrderService = shopOrderService;
    }

    @GetMapping
    public List<Order> getOrders(@PathVariable Long shopId, @RequestParam Long sellerId) {
        return shopOrderService.getOrdersOfShop(sellerId, shopId);
    }

    @PutMapping("/{orderId}/shipping")
    public Order elaborateToShipping(@PathVariable Long shopId, @RequestParam Long sellerId, @RequestParam String trackingId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryDate) {
        return shopOrderService.elaborateOrder(sellerId, shopId, trackingId, estimatedDeliveryDate);
    }

    @PutMapping("/expired-deliveries")
    public List<Order> getExpiredDeliveries(@PathVariable Long shopId, @RequestParam Long sellerId) {
        return shopOrderService.getExpiredOrdersToConfirmDelivery(sellerId, shopId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId, @RequestParam Long sellerId) {
        shopOrderService.deleteOrder(sellerId, orderId);
    }
}
