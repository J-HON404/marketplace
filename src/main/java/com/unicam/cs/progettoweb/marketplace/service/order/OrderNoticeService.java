package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.repository.order.OrderNoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderNoticeService {

    private final OrderNoticeRepository orderNoticeRepository;

    public OrderNoticeService(OrderNoticeRepository orderNoticeRepository) {
        this.orderNoticeRepository = orderNoticeRepository;
    }

    public List<OrderNotice> getAllOrderNotices() {
        return orderNoticeRepository.findAll();
    }

    public OrderNotice getOrderNoticeById(Long id) {
        return orderNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public OrderNotice addOrderNotice(OrderNotice order) {
        return orderNoticeRepository.save(order);
    }

    public OrderNotice updateOrderNotice(Long id, OrderNotice orderNoticeDetails) {

        OrderNotice orderNotice = getOrderNoticeById(id);

        orderNotice.setText(orderNoticeDetails.getText());
        orderNotice.setTypeNotice(orderNoticeDetails.getTypeNotice());
        orderNotice.setOrder(orderNoticeDetails.getOrder());

        return orderNoticeRepository.save(orderNotice);
    }

    public void deleteOrderNotice(Long id) {
        orderNoticeRepository.deleteById(id);
    }
}
