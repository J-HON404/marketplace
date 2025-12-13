package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopOrderController {

    private final ShopOrderService shopOrderService;

    public ShopOrderController(ShopOrderService shopOrderService) {
        this.shopOrderService = shopOrderService;
    }

    @GetMapping("/{shopId}/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getOrders(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getOrdersOfShop(shopId)));
    }

    @GetMapping("/{shopId}/orders/by-status")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByShopIdAndStatus(@PathVariable Long shopId, @RequestParam OrderStatus orderStatus) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getOrdersByShopIdAndStatus(shopId, orderStatus)));
    }


    @PutMapping("/orders/{orderId}/shipping")
    public ResponseEntity<ApiResponse<Order>> elaborateToShipping(@PathVariable Long orderId, @RequestParam String trackingId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryDate) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.elaborateOrder(orderId, trackingId, estimatedDeliveryDate)));
    }

    @GetMapping("/{shopId}/orders/expired-deliveries")
    public ResponseEntity<ApiResponse<List<Order>>> getExpiredDeliveries(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getExpiredOrdersToConfirmDelivery(shopId)));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        shopOrderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
