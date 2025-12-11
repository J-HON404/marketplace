package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShopOrderService {

    private final OrderService orderService;
    private final ShopService shopService;

    public ShopOrderService(OrderService orderService, ShopService shopService) {
        this.orderService = orderService;
        this.shopService = shopService;
    }

    public List<Order> getOrdersOfShop(Long sellerId, Long shopId) {
        checkSellerOwnsShop(sellerId, shopId);
        return orderService.getOrdersByShopId(shopId);
    }

    public Order elaborateOrder(Long sellerId, Long orderId, String tracking, LocalDate estimatedDeliveryDate) {
        Order order = orderService.getOrderById(orderId);
        authorizeSellerForOrder(sellerId, order);
        if (order.getStatus() != OrderStatus.READY_TO_ELABORATING) {
            throw new RuntimeException("Cannot set shipping details. Order is not ready to be elaborated. Current state: " + order.getStatus());
        }
        order.setTrackingId(tracking);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setStatus(OrderStatus.SHIPPING_DETAILS_SET);
        return orderService.updateOrderStatus(orderId, OrderStatus.SHIPPING_DETAILS_SET); // salva aggiornamenti
    }

    public List<Order> getExpiredOrdersToConfirmDelivery(Long sellerId, Long shopId) {
        checkSellerOwnsShop(sellerId, shopId);
        return orderService.getExpiredOrdersByShopId(shopId);
    }

    public void deleteOrder(Long sellerId, Long orderId) {
        Order order = orderService.getOrderById(orderId);
        authorizeSellerForOrder(sellerId, order);
        orderService.deleteOrder(orderId);
    }

    private void checkSellerOwnsShop(Long sellerId, Long shopId) {
        if (!shopService.getShopById(shopId).getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Seller does not own this shop");
        }
    }

    private void authorizeSellerForOrder(Long sellerId, Order order) {
        if (!order.getShop().getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Seller not authorized to manage this order");
        }
    }
}
