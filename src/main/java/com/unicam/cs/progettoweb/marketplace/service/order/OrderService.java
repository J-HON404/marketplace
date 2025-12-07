package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderNoticeService orderNoticeService;

    public OrderService(OrderRepository orderRepository, OrderNoticeService orderNoticeService) {
        this.orderRepository = orderRepository;
        this.orderNoticeService = orderNoticeService;
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
        order.setOrderDate(java.time.LocalDateTime.now());
        Order saved = orderRepository.save(order);
        //settare altri parametri ordine come prezzo ecc...!!!!!!!
        orderNoticeService.createOrderNotice(saved.getId(), TypeOrderNotice.READY_TO_ELABORATING, "New order received from customer");
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
        orderNoticeService.createOrderNotice(saved.getId(), TypeOrderNotice.SHIPPING_DETAILS_SET, "Order shipped");
        return saved;
    }

    public Order signOrderAsConsigned(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Only SHIPPED orders can be marked as consigned");
        }
        order.setStatus(OrderStatus.CONSIGNED);
        Order saved = orderRepository.save(order);
        orderNoticeService.createOrderNotice(saved.getId(), TypeOrderNotice.CONFIRMED_DELIVERED, "Order consigned");
        return saved;
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(orderId);
        orderNoticeService.createOrderNotice(orderId, TypeOrderNotice.ORDER_DELETED, "Order deleted");
    }
}
