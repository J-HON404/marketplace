package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.stereotype.Component;

@Component
public class CustomerSecurity {

    private final OrderService orderService;

    public CustomerSecurity(OrderService orderService) {
        this.orderService = orderService;
    }

    public boolean isOwnerOfOrder(Long customerId, Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order.getCustomer().getId().equals(customerId);
    }

}
