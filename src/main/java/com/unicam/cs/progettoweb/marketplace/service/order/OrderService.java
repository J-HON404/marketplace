package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {

        Order order = getOrderById(id);

        order.setStatus(orderDetails.getStatus());
        order.setCustomer(orderDetails.getCustomer());
        order.setShop(orderDetails.getShop());
        order.setItems(orderDetails.getItems()); //Cascade persist
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotal(orderDetails.getTotal());
        order.setTrackingCode(orderDetails.getTrackingCode());
        order.setEstimatedDeliveryDate(orderDetails.getEstimatedDeliveryDate());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
