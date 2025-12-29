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

/**
 * Controller REST per la gestione della cronologia ordini di un negozio.
 */

@RestController
@RequestMapping("/api/shops/{shopId}/orders")
public class ShopOrderHistoryController {

    private final ShopOrderService shopOrderService;

    public ShopOrderHistoryController(ShopOrderService shopOrderService) {
        this.shopOrderService = shopOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getOrders(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getOrdersOfShop(shopId)));
    }

    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByStatus(@PathVariable Long shopId, @RequestParam OrderStatus orderStatus) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getOrdersByShopIdAndStatus(shopId, orderStatus)));
    }

    @PutMapping("/{orderId}/shipping")
    public ResponseEntity<ApiResponse<Order>> elaborateToShipping(@PathVariable Long shopId, @PathVariable Long orderId, @RequestParam String trackingId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryDate) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.elaborateOrder(shopId,orderId, trackingId, estimatedDeliveryDate)));
    }

    @GetMapping("/expired-deliveries")
    public ResponseEntity<ApiResponse<List<Order>>> getExpiredDeliveries(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopOrderService.getExpiredOrdersToConfirmDelivery(shopId)));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long shopId, @PathVariable Long orderId) {
        shopOrderService.deleteOrder(shopId,orderId);
        return ResponseEntity.noContent().build();
    }
}
