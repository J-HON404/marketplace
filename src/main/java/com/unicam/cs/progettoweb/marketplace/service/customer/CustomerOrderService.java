package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerOrderService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderNoticeService orderNoticeService;

    public CustomerOrderService(OrderService orderService, CustomerService customerService, OrderNoticeService orderNoticeService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.orderNoticeService = orderNoticeService;
    }

    private void ensureCustomerExists(Long customerId){
        customerService.getCustomerById(customerId);
    }

    public List<Order> getOrdersOfCustomer(Long customerId){
        ensureCustomerExists(customerId);
        return orderService.getOrdersByCustomerId(customerId);
    }

    public Order createOrder(Long customerId, Order orderDetails) {
        ensureCustomerExists(customerId);
        // associa il customer all'ordine
        orderDetails.setCustomer(customerService.getCustomerById(customerId));
        // salva l'ordine prima
        Order savedOrder = orderService.addOrder(orderDetails);
        // crea la notifica associata all'ordine salvato
        orderNoticeService.createOrderNotice(savedOrder.getId(), TypeOrderNotice.READY_TO_ELABORATING, "NEW ORDER");
        return savedOrder;
    }

    public void confirmDelivered(Long customerId, Long orderId){
        ensureCustomerExists(customerId);
        Order order = orderService.getOrderById(orderId);
        // controlla che l'ordine appartenga al customer
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer is not allowed to confirm delivery for this order");
        }
        orderNoticeService.createOrderNotice(orderId, TypeOrderNotice.CONFIRMED_DELIVERED, "CONFIRMED DELIVERED");
    }

}
