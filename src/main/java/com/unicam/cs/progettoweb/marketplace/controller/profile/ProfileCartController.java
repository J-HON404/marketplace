package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{profileId}/cart")
public class ProfileCartController {

    private final CartService cartService;

    public ProfileCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(@PathVariable Long profileId) {
        Cart cart = cartService.getUserCart(profileId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addProductToCart(@PathVariable Long profileId, @RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.addProduct(profileId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Cart>> updateProductQuantity(@PathVariable Long profileId, @RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.updateQuantity(profileId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Cart>> removeProductFromCart(@PathVariable Long profileId, @RequestParam Long productId) {
        cartService.removeProduct(profileId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long profileId) {
        cartService.clearCart(profileId);
        return ResponseEntity.noContent().build();
    }
}
