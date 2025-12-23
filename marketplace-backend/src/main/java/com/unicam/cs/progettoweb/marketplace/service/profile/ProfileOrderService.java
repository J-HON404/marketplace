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

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public List<Order> getOrdersOfProfile(Long profileId) {
        ensureProfileExists(profileId);
        return orderService.getOrdersByProfileId(profileId);
    }


    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public Order createOrderFromCart(Long profileId, Long shopId) {
        ensureProfileExists(profileId);
        Cart cart = cartService.getCart(profileId, shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.BAD_REQUEST, "Cart is empty"));
        cartService.lockProductsForCheckout(cart);
        List<OrderItem> orderItems = buildOrderItemsFromCart(cart);
        Profile profile = profileService.findProfileById(profileId);
        Order order = buildOrder(profile, cart, orderItems);
        Order createdOrder = orderService.createOrder(order);
        finalizeCheckout(cart);
        return createdOrder;
    }

    private List<OrderItem> buildOrderItemsFromCart(Cart cart) {
        return cart.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(item.getProduct());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getProduct().getPrice());
                    return orderItem;
                })
                .toList();
    }

    private Order buildOrder(Profile profile, Cart cart, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomer(profile);
        order.setShop(cart.getShop());
        order.setItems(items);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(calculateTotalPrice(items));
        items.forEach(item -> item.setOrder(order));
        return order;
    }

    private void finalizeCheckout(Cart cart) {
        shopProductService.decrementQuantityFromCartItems(cart.getItems());
        cartService.clearCart(cart.getUser().getId(), cart.getShop().getId());
    }

    private double calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @PreAuthorize("hasRole('CUSTOMER') and @customerSecurity.isOwnerOfOrder(principal.id, #orderId)")
    public void confirmDelivered(Long profileId, Long orderId) {
        ensureProfileExists(profileId);
        OrderStatus status = orderService.getOrderStatus(orderId);
        if (status == OrderStatus.SHIPPING_DETAILS_SET || status == OrderStatus.REMIND_DELIVERY) {
            orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED_DELIVERED);
        } else {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "Impossibile confermare la consegna: ordine in stato " + status);
        }
    }
}
