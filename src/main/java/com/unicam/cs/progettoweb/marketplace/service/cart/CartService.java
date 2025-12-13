package com.unicam.cs.progettoweb.marketplace.service.cart;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.cart.CartItem;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.repository.cart.CartRepository;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProfileService userService;

    public CartService(CartRepository cartRepository, ProductService productService, ProfileService userService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Cart getUserCart(Long profileId) {
        return cartRepository.findByUser_Id(profileId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(userService.findProfileById(profileId));
                    return cartRepository.save(cart);
                });
    }

    public Cart addProduct(Long profileId, Long productId, int quantity) {
        Cart cart = getUserCart(profileId);
        Product product = productService.getProductById(productId);
        productService.checkProductDateAvailability(product);
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }
        return cartRepository.save(cart);
    }

    public void removeProduct(Long profileId, Long productId) {
        Cart cart = getUserCart(profileId);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    public Cart updateQuantity(Long profileId, Long productId, int newQuantity) {
        Cart cart = getUserCart(profileId);
        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(newQuantity));
        return cartRepository.save(cart);
    }

    public void clearCart(Long profileId) {
        Cart cart = getUserCart(profileId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
