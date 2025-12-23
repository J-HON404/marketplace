package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderItem;
import com.unicam.cs.progettoweb.marketplace.service.CartService;
import com.unicam.cs.progettoweb.marketplace.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ShopOrderService {

    private final OrderService orderService;
    private final CartService cartService;

    public ShopOrderService(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getOrdersOfShop(Long shopId) {
        return orderService.getOrdersByShopId(shopId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getOrdersByShopIdAndStatus(Long shopId, OrderStatus status) {
        return orderService.getOrdersByShopIdAndStatus(shopId,status);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<OrderItem> getOrderItemsOfShop(Long shopId) {
        List<Order> orders = getOrdersOfShop(shopId);
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfOrder(principal.id, #orderId)")
    public Order elaborateOrder(Long shopId, Long orderId, String tracking, LocalDate estimatedDeliveryDate) {
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.FORBIDDEN, "Order does not belong to this shop.");
        }
        if (order.getStatus() != OrderStatus.READY_TO_ELABORATING) {
            throw new MarketplaceException(HttpStatus.NOT_FOUND,"Order is not ready for elaboration.");
        }
        return orderService.setShippingDetails(orderId, tracking, estimatedDeliveryDate);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Order> getExpiredOrdersToConfirmDelivery(Long shopId) {
        return orderService.getExpiredOrdersByShopId(shopId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId) and @shopSecurity.isProductBelongsToShop(#shopId,#productId)")
    public boolean existsProductInCustomersCartsForShop(Long shopId, Long productId){
        return cartService.existsProductInCustomersCartsForShop(shopId,productId);
    }


    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfOrder(principal.id, #orderId)")
    public void deleteOrder(Long shopId, Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (!order.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.FORBIDDEN, "Order does not belong to this shop.");
        }
        orderService.deleteOrder(orderId);
    }
}

