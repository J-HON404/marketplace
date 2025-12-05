package com.unicam.cs.progettoweb.marketplace.repository;

import com.unicam.cs.progettoweb.marketplace.model.Order;
import com.unicam.cs.progettoweb.marketplace.model.Customer;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByShop(Shop shop);
}
