package com.unicam.cs.progettoweb.marketplace.controller.order;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
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

    // Restituisce tutte le notifiche associate a un ordine
    @GetMapping
    public ResponseEntity<List<OrderNotice>> getNotices(@PathVariable Long orderId) {
        List<OrderNotice> notices = orderNoticeService.getNoticesForOrder(orderId);
        return ResponseEntity.ok(notices);
    }

}
