package com.unicam.cs.progettoweb.marketplace.service.order;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
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

    public OrderNotice getOrderNoticeById(Long orderNoticeId) {
        return orderNoticeRepository.findById(orderNoticeId)
                .orElseThrow(() -> new RuntimeException("Order notice not found with id: " + orderNoticeId));
    }

    public OrderNotice addOrderNotice(OrderNotice order) {
        return orderNoticeRepository.save(order);
    }

    public OrderNotice updateOrderNotice(Long orderNoticeId, OrderNotice orderNoticeDetails) {

        OrderNotice orderNotice = getOrderNoticeById(orderNoticeId);

        orderNotice.setText(orderNoticeDetails.getText());
        orderNotice.setTypeNotice(orderNoticeDetails.getTypeNotice());
        orderNotice.setOrder(orderNoticeDetails.getOrder());

        return orderNoticeRepository.save(orderNotice);
    }

    public void deleteOrderNotice(Long orderNoticeId) {
        orderNoticeRepository.deleteById(orderNoticeId);
    }
}
