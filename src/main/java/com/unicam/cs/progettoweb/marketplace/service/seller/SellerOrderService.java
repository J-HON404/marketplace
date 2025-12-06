package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SellerOrderService {

    private final OrderService orderService;
    private final SellerService sellerService;

    public SellerOrderService(OrderService orderService, SellerService sellerService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
    }

    public List<Order> getOrderOfSeller(Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        Long shopId = seller.getShop().getId();
        return orderService.getOrdersByShopId(shopId);
    }

    public Order updateOrder(Long orderId, String trackingId, LocalDate estimatedDeliveryDate) {
        Order order = orderService.getOrderById(orderId);
        // un seller può modificare solo gli ordini del suo shop
        Seller seller = sellerService.getSellerById(order.getShop().getId());
        if (!order.getShop().getId().equals(seller.getShop().getId())) {
            throw new RuntimeException("Seller cannot modify this order");
        }
        order.setTrackingId(trackingId);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return orderService.updateOrder(order.getId(), order);
    }

    public void deleteOrder(Long sellerId, Long orderId) {
        Seller seller = sellerService.getSellerById(sellerId);
        Order existingOrder = orderService.getOrderById(orderId);
        //un seller può solo eliminare ordini del suo shop
        if (!existingOrder.getShop().getId().equals(seller.getShop().getId())) {
            throw new RuntimeException("Seller not allowed to delete this order");
        }
        orderService.deleteOrder(orderId);
    }
}
