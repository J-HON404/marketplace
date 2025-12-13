package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileShopService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShopProductService {

    private final ProductService productService;
    private final ProfileShopService shopService;

    public ShopProductService(ProductService productService, ProfileShopService shopService) {
        this.productService = productService;
        this.shopService = shopService;
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Product> getProductsOfShop(Long shopId) {
        return productService.getProductsByShopId(shopId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Product> getProductsOfShopNotAvailable(Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return productService.getUnavailableProductsByShop(shop);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public List<Product> getFutureAvailableProducts(Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return productService.getFutureProductsByShop(shop, LocalDate.now());
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product createProduct(Long shopId, Product product) {
        Shop shop = shopService.getShopById(shopId);
        if (product.getShop() != null && product.getShop().getId() != null &&
                !product.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "this product belongs to another shop");
        }
        product.setShop(shop);
        return productService.addProduct(product);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product updateProduct(Long shopId, Long productId, Product updatedProduct) {
        Product existing = productService.getProductById(productId);
        ensureProductBelongsToShop(existing, shopId);
        return productService.updateProduct(existing.getId(), updatedProduct);
    }


    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public void deleteProduct(Long shopId, Long productId) {
        Product existing = productService.getProductById(productId);
        ensureProductBelongsToShop(existing, shopId);
        productService.deleteProduct(existing.getId());
    }

    private void ensureProductBelongsToShop(Product product, Long shopId) {
        if (!product.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "product does not belong to this shop");
        }
    }
}
