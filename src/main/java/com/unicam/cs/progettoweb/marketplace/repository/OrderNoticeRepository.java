package com.unicam.cs.progettoweb.marketplace.repository;

import com.unicam.cs.progettoweb.marketplace.model.OrderNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderNoticeRepository extends JpaRepository<OrderNotice,Long> {
    Optional<OrderNotice> findOrderNoticeById(Long id);
}
