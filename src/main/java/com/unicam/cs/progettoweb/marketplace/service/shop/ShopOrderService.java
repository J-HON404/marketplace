package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.events.OrderEvent;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShopOrderService {

    private final OrderService orderService;
    private final SellerService sellerService;
    private final ApplicationEventPublisher eventPublisher;

    public ShopOrderService(OrderService orderService, SellerService sellerService, ApplicationEventPublisher eventPublisher) {
        this.orderService = orderService;
        this.sellerService = sellerService;
        this.eventPublisher = eventPublisher;
    }

    private void checkShopAccess(Long sellerId, Long shopId) {
        Shop shop = sellerService.getSellerById(sellerId).getShop();
        if (!shop.getId().equals(shopId)) {
            throw new RuntimeException("Unauthorized shop access");
        }
    }

    public List<Order> getOrdersOfShop(Long sellerId, Long shopId) {
        checkShopAccess(sellerId, shopId);
        return orderService.getOrdersByShopId(shopId);
    }

    public Order elaborateOrder(Long sellerId, Long shopId, Long orderId, String tracking, LocalDate estimatedDeliveryDate) {
        checkShopAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        order.setTrackingId(tracking);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setStatus(OrderStatus.SHIPPED);
        Order saved = orderService.updateOrder(order);
        eventPublisher.publishEvent(new OrderEvent(orderId, TypeOrderNotice.SHIPPING_DETAILS_SET));
        return saved;
    }

    public Order signConsigned(Long sellerId, Long shopId, Long orderId) {
        checkShopAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        order.setStatus(OrderStatus.CONSIGNED);
        Order saved = orderService.updateOrder(order);
        eventPublisher.publishEvent(new OrderEvent(orderId, TypeOrderNotice.CONFIRMED_DELIVERED));
        return saved;
    }

    public void deleteOrder(Long sellerId, Long shopId, Long orderId) {
        checkShopAccess(sellerId, shopId);
        orderService.deleteOrder(orderId);
        eventPublisher.publishEvent(new OrderEvent(orderId, TypeOrderNotice.ORDER_DELETED));
    }
}

