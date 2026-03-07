package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.repository.OrderRepository;
import org.springframework.stereotype.Component;

/**
 * Componente di sicurezza per verificare i permessi dei clienti.
 */

@Component("customerSecurity")
public class CustomerSecurity {

    private final OrderRepository orderRepository;

    public CustomerSecurity(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean isOwnerOfOrder(Long customerId, Long orderId) {
        return orderRepository.existsByIdAndCustomer_Id(orderId, customerId);
    }
}

