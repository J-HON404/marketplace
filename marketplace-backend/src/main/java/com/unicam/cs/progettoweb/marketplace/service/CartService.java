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
import org.springframework.security.access.prepost.PreAuthorize;
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

    public CartService(CartRepository cartRepository, ProductService productService, ProfileService userService, SellerShopService sellerShopService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
        this.sellerShopService = sellerShopService;
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public Optional<Cart> getCart(Long profileId, Long shopId) {
        return cartRepository.findByUser_IdAndShop_Id(profileId, shopId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isProductBelongsToShop(#shopId,#productId)")
    public boolean existsProductInCustomersCartsForShop(Long shopId, Long productId) {
        return cartRepository.findByShop_Id(shopId).stream()
                .flatMap(cart -> cart.getItems().stream())
                .anyMatch(item -> item.getProduct().getId().equals(productId));
    }

    private Optional<Cart> getCartByProfile(Long profileId) {
        return cartRepository.findByUser_Id(profileId);
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public Cart createCart(Long profileId, Long shopId) {
        Cart cart = new Cart();
        cart.setUser(userService.findProfileById(profileId));
        cart.setShop(sellerShopService.getShopById(shopId));
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public Cart addProduct(Long profileId, Long shopId, Long productId, int quantityToAdd) {
        Cart cart = prepareCartForShop(profileId, shopId);
        Product product = productService.getProductById(productId);
        int quantityInCart = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .mapToInt(CartItem::getQuantity)
                .sum();
        verifyProductStock(product, quantityInCart, quantityToAdd);
        updateCartItems(cart, product, quantityToAdd);
        return cartRepository.save(cart);
    }

    private Cart prepareCartForShop(Long profileId, Long shopId) {
        Cart cart = getCartByProfile(profileId).orElseGet(() -> createCart(profileId, shopId));
        if (!cart.getShop().getId().equals(shopId)) {
            cart.getItems().clear();
            cart.setShop(sellerShopService.getShopById(shopId));
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    private void verifyProductStock(Product product, int quantityInCart, int quantityToAdd) {
        productService.checkProductDateAvailability(product);
        int totalRequested = quantityInCart + quantityToAdd;
        if (totalRequested > product.getQuantity()) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "Quantità non disponibile per " + product.getName() +
                            ". Nel carrello hai già " + quantityInCart + " unità");
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

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public Cart updateCartProductQuantity(Long profileId, Long shopId, Long productId, int newQuantity) {
        Cart cart = getCart(profileId, shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "Carrello non trovato"));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "Prodotto non presente nel carrello"));
        verifyProductStock(item.getProduct(), 0, newQuantity);
        item.setQuantity(newQuantity);
        return cartRepository.save(cart);
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public void removeProduct(Long profileId, Long shopId, Long productId) {
        getCart(profileId, shopId).ifPresent(cart -> {
            cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
            cartRepository.save(cart);
        });
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #profileId")
    public void clearCart(Long profileId, Long shopId) {
        getCart(profileId, shopId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }

    @PreAuthorize("hasRole('CUSTOMER') and principal.id == #cart.user.id")
    public void lockProductsForCheckout(Cart cart) {
        cart.getItems().forEach(item -> {
            Product lockedProduct = productService.lockProductById(item.getProduct().getId());
            verifyProductStock(lockedProduct, 0, item.getQuantity());
            item.setProduct(lockedProduct);
        });
    }
}
