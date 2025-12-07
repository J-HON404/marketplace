package com.unicam.cs.progettoweb.marketplace.controller.customer;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.customer.CustomerOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/orders")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService){
        this.customerOrderService=customerOrderService;
    }

    @GetMapping
    public List<Order> getOrdersOfCustomer(Long customerId){
        return customerOrderService.getOrdersOfCustomer(customerId);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@PathVariable Long customerId, @RequestBody Order orderDetails) {
        Order createdOrder = customerOrderService.createOrder(customerId, orderDetails);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{orderId}/confirm-delivered")
    public void confirmDelivered(@PathVariable Long customerId, @PathVariable Long orderId){
        customerOrderService.confirmDelivered(customerId, orderId);
    }

}
