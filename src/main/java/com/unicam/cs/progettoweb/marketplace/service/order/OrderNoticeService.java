package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderNoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderNoticeService {

    private final OrderNoticeRepository orderNoticeRepository;
    private final OrderService orderService;

    public OrderNoticeService(OrderNoticeRepository orderNoticeRepository, OrderService orderService) {
        this.orderNoticeRepository = orderNoticeRepository;
        this.orderService = orderService;
    }

    public List<OrderNotice> getNoticesForOrder(Long orderId) {
        return orderNoticeRepository.findOrderNoticeByOrderId(orderId);
    }

    public OrderNotice createOrderNotice(Long orderId, TypeOrderNotice type, String text) {
        Order order = orderService.getOrderById(orderId);
        OrderNotice notice = new OrderNotice();
        notice.setOrder(order);
        notice.setTypeNotice(type);
        notice.setText(text);
        return orderNoticeRepository.save(notice);
    }
}

