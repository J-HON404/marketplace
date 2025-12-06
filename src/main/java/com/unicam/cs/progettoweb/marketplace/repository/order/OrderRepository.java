package com.unicam.cs.progettoweb.marketplace.repository.order;

import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.customer.Customer;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByShopId(Long shopId);
}
