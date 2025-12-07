package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShopOrderService {

    private final OrderService orderService;
    private final SellerService sellerService;
    private final OrderNoticeService orderNoticeService;

    public ShopOrderService(OrderService orderService, SellerService sellerService, OrderNoticeService orderNoticeService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
        this.orderNoticeService = orderNoticeService;
    }

    private void validateSellerAccess(Long sellerId, Long shopId) {
        Shop shop = sellerService.getSellerById(sellerId).getShop();
        if (!shop.getId().equals(shopId)) {
            throw new RuntimeException("Seller is not allowed to manage this shop");
        }
    }

    public List<Order> getOrdersOfShop(Long sellerId, Long shopId) {
        validateSellerAccess(sellerId, shopId);
        return orderService.getOrdersByShopId(shopId);
    }

    public Order updateOrder(Long sellerId, Long shopId, Long orderId, String trackingId, LocalDate estimatedDeliveryDate) {
        validateSellerAccess(sellerId, shopId);
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Order does not belong to this shop");
        }
        order.setTrackingId(trackingId);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        orderNoticeService.createOrderNotice(orderId, TypeOrderNotice.SHIPPING_DETAILS_SET, "ORDER PROCESSED AND READY TO SHIPPING");
        return orderService.updateOrder(orderId, order);
    }

    public void deleteOrder(Long sellerId, Long shopId, Long orderId) {
        validateSellerAccess(sellerId, shopId);
        Order existingOrder = orderService.getOrderById(orderId);
        if (!existingOrder.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Order does not belong to this shop");
        }
        orderNoticeService.createOrderNotice(orderId,TypeOrderNotice.ORDER_DELETED,"ORDER CANCELED DUE TO PROBLEMS");
        orderService.deleteOrder(orderId);
    }

}