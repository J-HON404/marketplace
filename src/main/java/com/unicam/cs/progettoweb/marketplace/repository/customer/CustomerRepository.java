package com.unicam.cs.progettoweb.marketplace.repository.customer;

import com.unicam.cs.progettoweb.marketplace.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
