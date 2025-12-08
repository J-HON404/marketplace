package com.unicam.cs.progettoweb.marketplace.service.cart;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.cart.CartItem;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.repository.cart.CartRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.service.profile.DefaultProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final DefaultProfileService userService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, DefaultProfileService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
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
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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

    public Cart removeProduct(Long profileId, Long productId) {
        Cart cart = getUserCart(profileId);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
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