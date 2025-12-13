package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
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
public class CustomerOrderService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final CartService cartService;

    public CustomerOrderService(OrderService orderService, CustomerService customerService, CartService cartService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.cartService = cartService;
    }

    private void ensureCustomerExists(Long customerId) {
        customerService.getCustomerById(customerId);
    }

    public List<Order> getOrdersOfCustomer(Long customerId) {
        ensureCustomerExists(customerId);
        return orderService.getOrdersByCustomerId(customerId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Order createOrder(Long customerId, Order orderDetails) {
        orderDetails.setCustomer(customerService.getCustomerById(customerId));
        return orderService.createOrder(orderDetails);
    }

    @Transactional
    public Order createOrderFromCart(Long customerId){
        ensureCustomerExists(customerId);
        Cart customerCart = cartService.getUserCart(customerId);
        if(customerCart.getItems().isEmpty()){
            throw new MarketplaceException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        List<OrderItem> orderItems = getOrderItemsFromCart(customerCart);
        Order order = new Order();
        order.setCustomer(customerService.getCustomerById(customerId));
        order.setShop(customerCart.getShop());
        order.setItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(getTotalPriceFromOrderItems(orderItems));
        orderItems.forEach(item -> item.setOrder(order));
        Order createdOrder = orderService.createOrder(order);
        cartService.clearCart(customerId);
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
    public void confirmDelivered(Long customerId, Long orderId) {
        ensureCustomerExists(customerId);
        orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED_DELIVERED);
    }
}
