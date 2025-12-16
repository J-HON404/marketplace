package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.cart.CartService;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{profileId}/shops/{shopId}/cart")
public class ProfileCartController {

    private final CartService cartService;
    private final ProfileOrderService profileOrderService;

    public ProfileCartController(CartService cartService, ProfileOrderService profileOrderService) {
        this.cartService = cartService;
        this.profileOrderService = profileOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(@PathVariable Long profileId, @PathVariable Long shopId) {
        Cart cart = cartService.getCart(profileId, shopId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Cart>> createCart(@PathVariable Long profileId, @PathVariable Long shopId) {
        Cart cart = cartService.createCart(profileId, shopId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addProductToCart(@PathVariable Long profileId, @PathVariable Long shopId, @RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.addProduct(profileId, shopId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Order>> checkout(@PathVariable Long profileId, @PathVariable Long shopId) {
        Order order = profileOrderService.createOrderFromCart(profileId, shopId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Cart>> updateProductQuantity(@PathVariable Long profileId, @PathVariable Long shopId, @RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.updateQuantity(profileId, shopId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long profileId, @PathVariable Long shopId, @RequestParam Long productId) {
        cartService.removeProduct(profileId, shopId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long profileId, @PathVariable Long shopId) {
        cartService.clearCart(profileId, shopId);
        return ResponseEntity.noContent().build();
    }
}
