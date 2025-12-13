package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShopOrderService {

    private final OrderService orderService;

    public ShopOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getOrdersOfShop(Long shopId) {
        return orderService.getOrdersByShopId(shopId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getOrdersByShopIdAndStatus(Long shopId, OrderStatus status) {
        return orderService.getOrdersByShopIdAndStatus(shopId,status);
    }

    @PreAuthorize("@shopSecurity.isSellerOfOrder(principal.id, #orderId)")
    public Order elaborateOrder(Long orderId, String tracking, LocalDate estimatedDeliveryDate) {
        Order order = orderService.getOrderById(orderId);
        if (order.getStatus() != OrderStatus.READY_TO_ELABORATING) {
            throw new MarketplaceException(HttpStatus.NOT_FOUND,"order is not ready for elaboration.");
        }
      return orderService.setShippingDetails(orderId,tracking,estimatedDeliveryDate);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getExpiredOrdersToConfirmDelivery(Long shopId) {
        return orderService.getExpiredOrdersByShopId(shopId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfOrder(principal.id, #orderId)")
    public void deleteOrder(Long orderId) {
        orderService.deleteOrder(orderId);
    }
}

