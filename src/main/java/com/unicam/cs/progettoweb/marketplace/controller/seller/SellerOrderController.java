package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerOrderService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sellers/{sellerId}/orders")
public class SellerOrderController {

    private final SellerService sellerService;
    private final SellerOrderService sellerOrderService;

    public SellerOrderController(SellerService sellerService, SellerOrderService sellerOrderService) {
        this.sellerService = sellerService;
        this.sellerOrderService = sellerOrderService;
    }

    @GetMapping
    public List<Order> getOrdersOfSeller(@PathVariable Long sellerId) {
        return sellerOrderService.getOrderOfSeller(sellerId);
    }

    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable Long orderId,
                             @RequestParam String trackingCode,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryDate) {
        // sellerId passato per eventuali controlli nel service
        return sellerOrderService.updateOrder(orderId, trackingCode, estimatedDeliveryDate);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrderOfSeller(@PathVariable Long sellerId,
                                    @PathVariable Long orderId) {
        List<Order> orders = sellerOrderService.getOrderOfSeller(sellerId);
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                // un seller può eliminare solo ordini del suo shop
                if (!order.getShop().getId().equals(sellerService.getSellerById(sellerId).getShop().getId())) {
                    throw new RuntimeException("Seller cannot delete this order");
                }
                sellerOrderService.deleteOrder(sellerId, orderId);
                return;
            }
        }
        throw new RuntimeException("Order not found for this seller");
    }

}
