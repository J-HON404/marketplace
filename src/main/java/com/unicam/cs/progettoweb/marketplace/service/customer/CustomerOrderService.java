package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.events.OrderEvent;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerOrderService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerOrderService(OrderService orderService, CustomerService customerService, ApplicationEventPublisher eventPublisher) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.eventPublisher = eventPublisher;
    }

    private void ensureCustomerExists(Long customerId) {
        customerService.getCustomerById(customerId);
    }

    public List<Order> getOrdersOfCustomer(Long customerId) {
        ensureCustomerExists(customerId);
        return orderService.getOrdersByCustomerId(customerId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE) //accesso db atomico!
    public Order createOrder(Long customerId, Order orderDetails) {
        orderDetails.setCustomer(customerService.getCustomerById(customerId));
        Order saved = orderService.createOrder(orderDetails);
        eventPublisher.publishEvent(new OrderEvent(saved.getId(), TypeOrderNotice.READY_TO_ELABORATING));
        return saved;
    }

    public void confirmDelivered(Long customerId, Long orderId) {
        ensureCustomerExists(customerId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Unauthorized");
        }
        order.setStatus(OrderStatus.CONSIGNED);
        orderService.updateOrder(order);
        eventPublisher.publishEvent(new OrderEvent(orderId, TypeOrderNotice.CONFIRMED_DELIVERED)
        );
    }
}
