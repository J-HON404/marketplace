package com.unicam.cs.progettoweb.marketplace.repository.order;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderNoticeRepository extends JpaRepository<OrderNotice,Long> {
    List<OrderNotice> findByOrder_Id(Long orderId);
}
