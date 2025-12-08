package com.unicam.cs.progettoweb.marketplace.repository.order;

import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByShopId(Long shopId);
    List<Order> findByShopIdAndStatusAndEstimatedDeliveryDateBefore(Long shopId, OrderStatus status, LocalDate date);
}
