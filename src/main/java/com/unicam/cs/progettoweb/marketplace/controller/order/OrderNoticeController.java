package com.unicam.cs.progettoweb.marketplace.controller.order;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/notices")
public class OrderNoticeController {

    private final OrderNoticeService orderNoticeService;

    public OrderNoticeController(OrderNoticeService orderNoticeService) {
        this.orderNoticeService = orderNoticeService;
    }

    @GetMapping
    public List<OrderNotice> getNotices(@PathVariable Long orderId) {
        return orderNoticeService.getNoticesForOrder(orderId);
    }

    @PostMapping
    public ResponseEntity<OrderNotice> createNotice(@PathVariable Long orderId, @RequestParam TypeOrderNotice type, @RequestParam(required = false) String text) {
        OrderNotice notice = orderNoticeService.createOrderNotice(orderId, type, text);
        return ResponseEntity.ok(notice);
    }
}

