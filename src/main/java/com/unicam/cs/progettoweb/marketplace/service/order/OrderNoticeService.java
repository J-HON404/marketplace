package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.events.OrderEvent;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationService;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderNoticeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderNoticeService {

    private final OrderNoticeRepository orderNoticeRepository;
    private final OrderService orderService;
    private final NotificationService notificationService;

    public OrderNoticeService(OrderNoticeRepository orderNoticeRepository, OrderService orderService, NotificationService notificationService) {
        this.orderNoticeRepository = orderNoticeRepository;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        Order order = orderService.getOrderById(event.getOrderId());
        OrderNotice notice = new OrderNotice(order, event.getType(), "Event: " + event.getType());
        orderNoticeRepository.save(notice);
        notificationService.notifyNotice(notice);
    }

    public List<OrderNotice> getNoticesForOrder(Long orderId) {
        return orderNoticeRepository.findByOrder_Id(orderId);
    }

    public OrderNotice createOrderNotice(Long orderId, TypeOrderNotice type, String text) {
        Order order = orderService.getOrderById(orderId); // <-- usa il servizio
        OrderNotice notice = new OrderNotice(order, type, text != null ? text : "Event: " + type);
        return orderNoticeRepository.save(notice);
    }

    public List<OrderNotice> findAllOrdersAndNoticesOfProfile(Long profileId) {
        List<Order> ordersOfProfile = orderService.getOrdersForProfile(profileId);
        if (ordersOfProfile.isEmpty()) {
            throw new RuntimeException("No orders found for profile: " + profileId);
        }
        List<OrderNotice> allNotices = new ArrayList<>();
        for (Order order : ordersOfProfile) {
            List<OrderNotice> notices = orderNoticeRepository.findByOrder_Id(order.getId());
            allNotices.addAll(notices);
        }
        return allNotices;
    }
}
