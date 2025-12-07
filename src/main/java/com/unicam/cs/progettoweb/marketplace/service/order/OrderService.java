package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.events.OrderEvent;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getOrdersByShopId(Long shopId) {
        return orderRepository.findByShopId(shopId);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.READY);
        order.setOrderDate(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderEvent(saved.getId(), TypeOrderNotice.READY_TO_ELABORATING));
        return saved;
    }

    public Order elaborateOrderToShipping(Order order, String trackingId, LocalDate estimatedDeliveryDate) {
        if (order.getStatus() != OrderStatus.READY) {
            throw new RuntimeException("Only READY orders can be processed for shipping");
        }
        order.setTrackingId(trackingId);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setStatus(OrderStatus.SHIPPED);
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderEvent(saved.getId(), TypeOrderNotice.SHIPPING_DETAILS_SET));
        return saved;
    }

    public Order signOrderAsConsigned(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Only SHIPPED orders can be marked as consigned");
        }
        order.setStatus(OrderStatus.CONSIGNED);
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderEvent(saved.getId(), TypeOrderNotice.CONFIRMED_DELIVERED));
        return saved;
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(orderId);
        eventPublisher.publishEvent(new OrderEvent(orderId, TypeOrderNotice.ORDER_DELETED));
    }
}
