package com.unicam.cs.progettoweb.marketplace.controller.profile;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.service.cart.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{profileId}/cart")
public class ProfileCartController {

    private final CartService cartService;

    public ProfileCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getCart(@PathVariable Long profileId) {
        return cartService.getUserCart(profileId);
    }

    @PostMapping("/add")
    public Cart addProductToCart(@PathVariable Long profileId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addProduct(profileId, productId, quantity);
    }

    @PutMapping("/update")
    public Cart updateProductQuantity(@PathVariable Long profileId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.updateQuantity(profileId, productId, quantity);
    }

    @DeleteMapping("/remove")
    public Cart removeProductFromCart(@PathVariable Long profileId, @RequestParam Long productId) {
        return cartService.removeProduct(profileId, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@PathVariable Long profileId) {
        cartService.clearCart(profileId);
    }
}
