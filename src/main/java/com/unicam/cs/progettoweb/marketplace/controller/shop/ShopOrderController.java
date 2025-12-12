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
    public List<Order> getOrders(@PathVariable Long shopId) {
        return shopOrderService.getOrdersOfShop(shopId);
    }

    @PutMapping("/{orderId}/shipping")
    public Order elaborateToShipping(@PathVariable Long orderId, @RequestParam String trackingId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryDate) {
        return shopOrderService.elaborateOrder(orderId, trackingId, estimatedDeliveryDate);
    }

    @GetMapping("/expired-deliveries")
    public List<Order> getExpiredDeliveries(@PathVariable Long shopId) {
        return shopOrderService.getExpiredOrdersToConfirmDelivery(shopId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        shopOrderService.deleteOrder(orderId);
    }
}
