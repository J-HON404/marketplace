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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersByShopId(Long shopId) {
        return orderRepository.findByShopId(shopId);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getExpiredDeliveryConfirmation(Long shopId){
        return orderRepository.findByShopIdAndStatusAndEstimatedDeliveryDateBefore(shopId, OrderStatus.SHIPPED, LocalDate.now());
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
