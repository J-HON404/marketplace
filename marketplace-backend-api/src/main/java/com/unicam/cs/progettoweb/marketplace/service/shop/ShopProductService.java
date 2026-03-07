package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.dto.product.ProductRequest;
import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.cart.CartItem;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderItem;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Servizio per la gestione dei prodotti di uno shop.
 * * Questo servizio consente di:
 * - Recuperare prodotti di uno shop (tutti, non disponibili, disponibili in futuro)
 * - Creare, aggiornare e cancellare prodotti dello shop
 * - Gestire la quantità dei prodotti
 */

@Service
public class ShopProductService {

    private final ProductService productService;
    private final SellerShopService shopService;
    private final ShopOrderService shopOrderService;

    public ShopProductService(ProductService productService, SellerShopService shopService, ShopOrderService shopOrderService) {
        this.productService = productService;
        this.shopService = shopService;
        this.shopOrderService = shopOrderService;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Product> getProductsOfShop(Long shopId) {
        return productService.getProductsByShopId(shopId);
    }

    @PreAuthorize("isAuthenticated() and @shopSecurity.isProductBelongsToShop(#shopId,#productId)")
    public Product getProductOfShop(Long shopId, Long productId) {
        return productService.getProductById(productId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Product> getProductsOfShopNotAvailable(Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return productService.getUnavailableProductsByShop(shop);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Product> getFutureAvailableProducts(Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return productService.getFutureProductsByShop(shop, LocalDate.now());
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product createProduct(Long shopId, ProductRequest productRequest) {
        Shop shop = shopService.getShopById(shopId);
        Product product = new Product();
        product.setName(productRequest.name);
        product.setDescription(productRequest.description);
        product.setPrice(productRequest.price);
        product.setQuantity(productRequest.quantity);
        product.setAvailabilityDate(productRequest.availabilityDate);
        product.setShop(shop);
        blockProductIfNotAddable(product,shopId);
        return productService.addProduct(product);
    }

    private void blockProductIfNotAddable(Product product, Long shopId){
        if (product.getShop() != null && product.getShop().getId() != null &&
                !product.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "This product belongs to another shop");
        }
        if(productService.checkIfProductAlreadyExists(product.getName())){
            throw new MarketplaceException(HttpStatus.CONFLICT, "Product's name already exists");
        }
    }

    private void blockProductIfNotRemovable(Long shopId, Long productId) {
        if (shopOrderService.existsProductInCustomersCartsForShop(shopId, productId)) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "This product cannot be deleted because it's in a customer cart.");
        }
        List<OrderItem> orderItems = shopOrderService.getOrderItemsOfShop(shopId);
        boolean existsInOrders = orderItems.stream()
                .anyMatch(orderItem -> orderItem.getProduct().getId().equals(productId));
        if(existsInOrders){
            throw new MarketplaceException(HttpStatus.BAD_REQUEST,
                    "This product cannot be deleted because it has been ordered.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void decrementQuantityFromCartItems(List<CartItem> items) {
        for (CartItem item : items) {
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() - item.getQuantity();
            productService.updateProductQuantity(product.getId(), newQuantity);
        }
    }


    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId) and @shopSecurity.isProductBelongsToShop(#shopId,#productId)")
    public Product updateProduct(Long shopId, Long productId, Product updatedProduct) {
        Product product = productService.getProductById(productId);
        return productService.updateProduct(product.getId(), updatedProduct);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId) and @shopSecurity.isProductBelongsToShop(#shopId,#productId)")
    public void deleteProduct(Long shopId, Long productId) {
        blockProductIfNotRemovable(shopId,productId);
        Product product = productService.getProductById(productId);
        productService.deleteProduct(product.getId());
    }



}
