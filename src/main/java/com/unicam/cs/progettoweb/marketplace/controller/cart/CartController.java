package com.unicam.cs.progettoweb.marketplace.controller.cart;

import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long profileId) {
        Cart cart = cartService.getUserCart(profileId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{profileId}/items")
    public ResponseEntity<Cart> addProduct(
            @PathVariable Long profileId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        Cart updatedCart = cartService.addProduct(profileId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{profileId}/items/{productId}")
    public ResponseEntity<Cart> removeProduct(
            @PathVariable Long profileId,
            @PathVariable Long productId
    ) {
        Cart updatedCart = cartService.removeProduct(profileId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/{profileId}/items/{productId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable Long profileId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        Cart updatedCart = cartService.updateQuantity(profileId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long profileId) {
        cartService.clearCart(profileId);
        return ResponseEntity.noContent().build();
    }
}

