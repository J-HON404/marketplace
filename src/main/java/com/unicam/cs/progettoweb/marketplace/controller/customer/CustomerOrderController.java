package com.unicam.cs.progettoweb.marketplace.controller.customer;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.customer.CustomerOrderService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersOfCustomer(@PathVariable Long customerId){
        List<Order> orders = customerOrderService.getOrdersOfCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@PathVariable Long customerId, @RequestBody Order orderDetails) {
        Order order = customerOrderService.createOrder(customerId, orderDetails);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/from-cart")
    public ResponseEntity<ApiResponse<Order>> createOrderFromCart(@PathVariable Long customerId){
        Order order = customerOrderService.createOrderFromCart(customerId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PutMapping("/{orderId}/confirm-delivered")
    public ResponseEntity<ApiResponse<Void>> confirmDelivered(@PathVariable Long customerId, @PathVariable Long orderId){
        customerOrderService.confirmDelivered(customerId, orderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
