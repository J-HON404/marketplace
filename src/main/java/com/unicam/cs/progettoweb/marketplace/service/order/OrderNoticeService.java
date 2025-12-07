package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.events.OrderEvent;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderNoticeRepository;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderNoticeService {

    private final OrderNoticeRepository orderNoticeRepository;
    private final OrderRepository orderRepository;

    public OrderNoticeService(OrderNoticeRepository orderNoticeRepository, OrderRepository orderRepository) {
        this.orderNoticeRepository = orderNoticeRepository;
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
        OrderNotice notice = new OrderNotice(order, event.getType(), "Event: " + event.getType());
        orderNoticeRepository.save(notice);
    }

    public List<OrderNotice> getNoticesForOrder(Long orderId) {
        return orderNoticeRepository.findByOrderId(orderId);
    }

    public OrderNotice createOrderNotice(Long orderId, TypeOrderNotice type, String text) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        OrderNotice notice = new OrderNotice(order, type, text != null ? text : "Event: " + type);
        return orderNoticeRepository.save(notice);
    }
}
