package com.unicam.cs.progettoweb.marketplace.service.cart;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.cart.CartItem;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.repository.cart.CartRepository;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProfileService userService;
    private final SellerShopService sellerShopService;

    public CartService(CartRepository cartRepository, ProductService productService, ProfileService userService, SellerShopService sellerShopService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
        this.sellerShopService = sellerShopService;
    }

    public Optional<Cart> getCart(Long profileId, Long shopId) {
        return cartRepository.findByUser_IdAndShop_Id(profileId, shopId);
    }

    public Cart createCart(Long profileId, Long shopId) {
        Cart cart = new Cart();
        cart.setUser(userService.findProfileById(profileId));
        cart.setShop(sellerShopService.findShopById(shopId));
        return cartRepository.save(cart);
    }

    public Cart addProduct(Long profileId, Long shopId, Long productId, int quantity) {
        Cart cart = getCart(profileId, shopId).orElseGet(() -> createCart(profileId, shopId));
        Product product = productService.getProductById(productId);
        productService.checkProductDateAvailability(product);
        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            CartItem item = new CartItem();
                            item.setCart(cart);
                            item.setProduct(product);
                            item.setQuantity(quantity);
                            cart.getItems().add(item);
                        }
                );
        return cartRepository.save(cart);
    }

    public void removeProduct(Long profileId, Long shopId, Long productId) {
        Cart cart = getCart(profileId, shopId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    public Cart updateQuantity(Long profileId, Long shopId, Long productId, int newQuantity) {
        Cart cart = getCart(profileId, shopId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(newQuantity));
        return cartRepository.save(cart);
    }

    public void clearCart(Long profileId, Long shopId) {
        Cart cart = getCart(profileId, shopId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
