package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
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

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getOrdersByShopId(Long shopId) {
        return orderRepository.findByShopId(shopId);
    }

    public List<Order> getOrdersByCustomerId(Long customerId){
        return orderRepository.findByCustomerId(customerId);
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, Order orderDetails) {

        Order order = getOrderById(orderId);

        order.setStatus(orderDetails.getStatus());
        order.setCustomer(orderDetails.getCustomer());
        order.setShop(orderDetails.getShop());
        order.setItems(orderDetails.getItems()); //Cascade persist
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotal(orderDetails.getTotal());
        order.setTrackingId(orderDetails.getTrackingId());
        order.setEstimatedDeliveryDate(orderDetails.getEstimatedDeliveryDate());

        return orderRepository.save(order);
    }

    /*
    public void advanceOrderStatus(Long orderId){
        Order order = getOrderById(orderId);
        switch(order.getStatus()) {
            case CREATED -> order.setStatus(OrderStatus.READY);
            case READY -> order.setStatus(OrderStatus.SHIPPED);
            case SHIPPED -> order.setStatus(OrderStatus.CONSIGNED);
            case CONSIGNED -> throw new RuntimeException("Order already consigned");
        }
        orderRepository.save(order);
    }
       */

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
