package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShopOrderService {

    private final OrderService orderService;
    private final SellerService sellerService;

    public ShopOrderService(OrderService orderService, SellerService sellerService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
    }

    private void validateSellerAccess(Long sellerId, Long shopId) {
        Shop shop = sellerService.getSellerById(sellerId).getShop();
        if (!shop.getId().equals(shopId)) {
            throw new RuntimeException("Seller cannot manage this shop");
        }
    }

    public List<Order> getOrdersOfShop(Long sellerId, Long shopId) {
        validateSellerAccess(sellerId, shopId);
        return orderService.getOrdersByShopId(shopId);
    }

    public Order elaborateOrderToShipping(Long sellerId, Long shopId, Long orderId, String trackingId, LocalDate estimatedDeliveryDate) {
        validateSellerAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Order does not belong to shop");
        }
        return orderService.elaborateOrderToShipping(order, trackingId, estimatedDeliveryDate);
    }

    public Order signOrderAsConsigned(Long sellerId, Long shopId, Long orderId) {
        validateSellerAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Order does not belong to shop");
        }
        return orderService.signOrderAsConsigned(order);
    }

    public void deleteOrder(Long sellerId, Long shopId, Long orderId) {
        validateSellerAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Order does not belong to shop");
        }
        orderService.deleteOrder(orderId);
    }
}
