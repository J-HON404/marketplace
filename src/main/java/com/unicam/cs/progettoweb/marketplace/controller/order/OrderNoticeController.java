package com.unicam.cs.progettoweb.marketplace.controller.order;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderNotices")
public class OrderNoticeController {

    private final OrderNoticeService orderNoticeService;

    public OrderNoticeController(OrderNoticeService orderNoticeService) {
        this.orderNoticeService = orderNoticeService;
    }

    @GetMapping
    public List<OrderNotice> getAllOrderNotices() {
        return orderNoticeService.getAllOrderNotices();
    }

    @GetMapping("/{orderNoticeId}")
    public ResponseEntity<OrderNotice> getOrderNotice(@PathVariable Long orderNoticeId) {
        OrderNotice orderNotice = orderNoticeService.getOrderNoticeById(orderNoticeId);
        return ResponseEntity.ok(orderNotice);
    }

    @PostMapping
    public ResponseEntity<OrderNotice> createOrderNotice(@RequestBody OrderNotice orderNotice) {
        OrderNotice createdOrderNotice = orderNoticeService.addOrderNotice(orderNotice);
        return ResponseEntity.ok(createdOrderNotice);
    }

    @PutMapping("/{orderNoticeId}")
    public ResponseEntity<OrderNotice> updateOrderNotice(@PathVariable Long orderNoticeId, @RequestBody OrderNotice orderNoticeDetails) {
        OrderNotice updatedOrderNotice = orderNoticeService.updateOrderNotice(orderNoticeId, orderNoticeDetails);
        return ResponseEntity.ok(updatedOrderNotice);
    }

    @DeleteMapping("/{orderNoticeId}")
    public ResponseEntity<Void> deleteOrderNotice(@PathVariable Long orderNoticeId) {
        orderNoticeService.deleteOrderNotice(orderNoticeId);
        return ResponseEntity.noContent().build();
    }
}
