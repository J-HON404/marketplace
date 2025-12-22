package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.enums.OrderStatus;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderItem;
import com.unicam.cs.progettoweb.marketplace.service.CartService;
import com.unicam.cs.progettoweb.marketplace.service.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class ProfileOrderService {

    private final OrderService orderService;
    private final ProfileService profileService;
    private final CartService cartService;
    private final ShopProductService shopProductService;

    public ProfileOrderService(OrderService orderService, ProfileService profileService, CartService cartService, ShopProductService shopProductService) {
        this.orderService = orderService;
        this.profileService = profileService;
        this.cartService = cartService;
        this.shopProductService = shopProductService;
    }

    private void ensureProfileExists(Long profileId) {
        profileService.findProfileById(profileId);
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId ")
    public List<Order> getOrdersOfProfile(Long profileId) {
        ensureProfileExists(profileId);
        return orderService.getOrdersByProfileId(profileId);
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId ")
    public Order createOrderFromCart(Long profileId, Long shopId) {
        ensureProfileExists(profileId);
        Cart customerCart = cartService.getCart(profileId, shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.BAD_REQUEST, "Cart is empty"));
        cartService.validateCartForCheckout(customerCart);
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
        shopProductService.decrementQuantityFromCartItems(customerCart.getItems());
        cartService.clearCart(profileId, shopId);
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

    @PreAuthorize("hasRole('CUSTOMER') and @customerSecurity.isOwnerOfOrder(principal.id, #orderId)")
    public void confirmDelivered(Long profileId, Long orderId) {
        ensureProfileExists(profileId);
        OrderStatus currentStatus = orderService.getOrderStatus(orderId);
        if (currentStatus == OrderStatus.SHIPPING_DETAILS_SET || currentStatus == OrderStatus.REMIND_DELIVERY) {
            orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED_DELIVERED);
        } else {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "Impossibile confermare la consegna: ordine in stato " + currentStatus + ")");
        }
    }
}