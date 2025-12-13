package com.unicam.cs.progettoweb.marketplace.controller.customer;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.customer.CustomerOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/orders")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService){
        this.customerOrderService = customerOrderService;
    }

    @GetMapping
    public List<Order> getOrdersOfCustomer(@PathVariable Long customerId){
        return customerOrderService.getOrdersOfCustomer(customerId);
    }

    @PostMapping
    public Order createOrder(@PathVariable Long customerId, @RequestBody Order orderDetails) {
        return customerOrderService.createOrder(customerId, orderDetails);
    }

    @PostMapping("/from-cart")
    public Order createOrderFromCart(@PathVariable Long customerId){
        return customerOrderService.createOrderFromCart(customerId);
    }

    @PutMapping("/{orderId}/confirm-delivered")
    public void confirmDelivered(@PathVariable Long customerId, @PathVariable Long orderId){
        customerOrderService.confirmDelivered(customerId, orderId);
    }
}
