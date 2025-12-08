package com.unicam.cs.progettoweb.marketplace.repository.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long customerId);
    List<Order> findByShop_Id(Long shopId);
    List<Order> findByShop_IdOrCustomer_Id(Long shopId, Long customerId);
    List<Order> findByShop_IdAndStatusAndEstimatedDeliveryDateBefore(Long shopId, OrderStatus status, LocalDate date);
}

