package com.unicam.cs.progettoweb.marketplace.service;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.cart.Cart;
import com.unicam.cs.progettoweb.marketplace.model.cart.CartItem;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.repository.CartRepository;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProfileService userService;
    private final SellerShopService sellerShopService;

    public CartService(CartRepository cartRepository, ProductService productService,
                       ProfileService userService, SellerShopService sellerShopService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
        this.sellerShopService = sellerShopService;
    }


    public Optional<Cart> getCart(Long profileId, Long shopId) {
        return cartRepository.findByUser_IdAndShop_Id(profileId, shopId);
    }


    private Optional<Cart> getCartByProfile(Long profileId) {
        return cartRepository.findByUser_Id(profileId);
    }

    public Cart createCart(Long profileId, Long shopId) {
        Cart cart = new Cart();
        cart.setUser(userService.findProfileById(profileId));
        cart.setShop(sellerShopService.getShopById(shopId));
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    public Cart addProduct(Long profileId, Long shopId, Long productId, int quantity) {
        Cart cart = prepareCart(profileId, shopId);
        Product product = productService.getProductById(productId);

        validateAvailability(cart, product, quantity);
        updateCartItems(cart, product, quantity);

        return cartRepository.save(cart);
    }

    private Cart prepareCart(Long profileId, Long shopId) {
        // Cerchiamo se l'utente ha UN carrello nel sistema
        Cart cart = getCartByProfile(profileId).orElseGet(() -> createCart(profileId, shopId));

        // Se il carrello trovato appartiene a un altro negozio, lo svuotiamo e cambiamo negozio
        if (!cart.getShop().getId().equals(shopId)) {
            cart.getItems().clear();
            cart.setShop(sellerShopService.getShopById(shopId));
            // Forza il salvataggio del cambio negozio prima di aggiungere nuovi oggetti
            cart = cartRepository.saveAndFlush(cart);
        }
        return cart;
    }

    private void validateAvailability(Cart cart, Product product, int quantity) {
        productService.checkProductDateAvailability(product);

        int alreadyInCart = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .mapToInt(CartItem::getQuantity)
                .sum();

        if (alreadyInCart + quantity > product.getQuantity()) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "Quantità non disponibile. In magazzino: " + product.getQuantity());
        }
    }

    private void updateCartItems(Cart cart, Product product, int quantity) {
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(quantity);
                            cart.getItems().add(newItem);
                        }
                );
    }

    public void removeProduct(Long profileId, Long shopId, Long productId) {
        getCart(profileId, shopId).ifPresent(cart -> {
            cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
            cartRepository.save(cart);
        });
    }

    public Cart updateQuantity(Long profileId, Long shopId, Long productId, int newQuantity) {
        Cart cart = getCart(profileId, shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "Carrello non trovato"));

        cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(newQuantity));

        return cartRepository.save(cart);
    }

    public void clearCart(Long profileId, Long shopId) {
        getCart(profileId, shopId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }
}