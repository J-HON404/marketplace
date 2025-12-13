package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderItem;
import com.unicam.cs.progettoweb.marketplace.service.cart.CartService;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileOrderService {

    private final OrderService orderService;
    private final ProfileService profileService;
    private final CartService cartService;

    public ProfileOrderService(OrderService orderService, ProfileService profileService, CartService cartService) {
        this.orderService = orderService;
        this.profileService = profileService;
        this.cartService = cartService;
    }

    private void ensureProfileExists(Long profileId) {
        profileService.findProfileById(profileId);
    }

    public List<Order> getOrdersOfProfile(Long profileId) {
        ensureProfileExists(profileId);
        return orderService.getOrdersByProfileId(profileId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Order createOrder(Long profileId, Order orderDetails) {
        Profile profile = profileService.findProfileById(profileId);
        orderDetails.setCustomer(profile);
        return orderService.createOrder(orderDetails);
    }

    @Transactional
    public Order createOrderFromCart(Long profileId){
        ensureProfileExists(profileId);
        Cart customerCart = cartService.getUserCart(profileId);
        if(customerCart.getItems().isEmpty()){
            throw new MarketplaceException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        List<OrderItem> orderItems = getOrderItemsFromCart(customerCart);
        Order order = new Order();
        Profile profile = profileService.findProfileById(profileId);
        order.setCustomer(profile);
        order.setShop(customerCart.getShop());
        order.setItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(getTotalPriceFromOrderItems(orderItems));
        orderItems.forEach(item -> item.setOrder(order));
        Order createdOrder = orderService.createOrder(order);
        cartService.clearCart(profileId);
        return createdOrder;
    }

    private List<OrderItem> getOrderItemsFromCart(Cart customerCart) {
        return customerCart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    return orderItem;
                })
                .toList();
    }

    private double getTotalPriceFromOrderItems(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @PreAuthorize("@customerSecurity.isOwnerOfOrder(principal.id, #orderId)")
    public void confirmDelivered(Long profileId, Long orderId) {
        ensureProfileExists(profileId);
        orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED_DELIVERED);
    }
}
