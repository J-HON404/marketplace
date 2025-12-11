package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CustomerOrderService {
    
    private final OrderService orderService;
    private final CustomerService customerService;


    public CustomerOrderService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    private void ensureCustomerExists(Long customerId) {
        customerService.getCustomerById(customerId);
    }

    public List<Order> getOrdersOfCustomer(Long customerId) {
        ensureCustomerExists(customerId);
        return orderService.getOrdersByCustomerId(customerId);
    }


    /*  accesso db atomico con lock! senza interruzioni durante la transazione
        più utenti possono tentare di acquistare contemporaneamente.
       ma se due transazioni modificano lo stesso prodotto nello stesso momento:
        Una transazione acquisisce il “lock” sul dato e procede e
        l’altra deve attendere finché la prima non termina.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Order createOrder(Long customerId, Order orderDetails) {
        orderDetails.setCustomer(customerService.getCustomerById(customerId));
        return orderService.createOrder(orderDetails);
    }

    public void confirmDelivered(Long customerId, Long orderId) {
        ensureCustomerExists(customerId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Unauthorized");
        }
        orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED_DELIVERED);
    }
}
