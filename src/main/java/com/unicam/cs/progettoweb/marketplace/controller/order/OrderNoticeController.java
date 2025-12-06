package com.unicam.cs.progettoweb.marketplace.controller.order;

import com.unicam.cs.progettoweb.marketplace.model.OrderNotice;
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

    @GetMapping("/{id}")
    public ResponseEntity<OrderNotice> getOrderNotice(@PathVariable Long id) {
        OrderNotice orderNotice = orderNoticeService.getOrderNoticeById(id);
        return ResponseEntity.ok(orderNotice);
    }

    @PostMapping
    public ResponseEntity<OrderNotice> createOrderNotice(@RequestBody OrderNotice orderNotice) {
        OrderNotice createdOrderNotice = orderNoticeService.addOrderNotice(orderNotice);
        return ResponseEntity.ok(createdOrderNotice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderNotice> updateOrderNotice(@PathVariable Long id, @RequestBody OrderNotice orderNoticeDetails) {
        OrderNotice updatedOrderNotice = orderNoticeService.updateOrderNotice(id, orderNoticeDetails);
        return ResponseEntity.ok(updatedOrderNotice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderNotice(@PathVariable Long id) {
        orderNoticeService.deleteOrderNotice(id);
        return ResponseEntity.noContent().build();
    }
}
