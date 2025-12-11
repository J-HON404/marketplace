package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    public List<Order> getOrdersByShopId(Long shopId) {
        return orderRepository.findByShop_Id(shopId);
    }

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.READY_TO_ELABORATING);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("OrderStatus cannot be null");
        }

        Order order = getOrderById(orderId);

        if (order.getStatus() == newStatus) {
            return order;
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByShopIdAndStatus(Long shopId, OrderStatus status) {
        return orderRepository.findByShop_IdAndStatus(shopId, status);
    }

    public List<Order> getExpiredOrdersByShopId(Long shopId) {
        return orderRepository.findByShop_IdAndStatusAndEstimatedDeliveryDateBefore(
                shopId,
                OrderStatus.SHIPPING_DETAILS_SET,
                LocalDate.now()
        );
    }

    public Order setShippingDetails(Long orderId, String trackingId, LocalDate estimatedDeliveryDate) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != OrderStatus.READY_TO_ELABORATING) {
            throw new RuntimeException("Cannot set shipping details. Order is not ready to be elaborated. Current state: " + order.getStatus());
        }
        order.setTrackingId(trackingId);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setStatus(OrderStatus.SHIPPING_DETAILS_SET);
        return orderRepository.save(order);
    }
}
