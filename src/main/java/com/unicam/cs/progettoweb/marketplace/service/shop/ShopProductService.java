package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShopProductService {

    private final ProductService productService;
    private final ShopService shopService;

    public ShopProductService(ProductService productService, ShopService shopService) {
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
        LocalDate today = LocalDate.now();
        return productService.getFutureProductsByShop(shop, today);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product createProduct(Long shopId, Product product) {
        Shop shop = shopService.getShopById(shopId);
        if (product.getShop() != null && product.getShop().getId() != null &&
                !product.getShop().getId().equals(shopId)) {
            throw new RuntimeException("This product belongs to another shop");
        }
        product.setShop(shop);
        return productService.addProduct(product);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product updateProduct(Long shopId, Long productId, Product updatedProduct) {
        Product existing = productService.getProductById(productId);
        if (!existing.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Product does not belong to this shop");
        }
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setAvailabilityDate(updatedProduct.getAvailabilityDate());
        return productService.updateProduct(productId, existing);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public void deleteProduct(Long shopId, Long productId) {
        Product existing = productService.getProductById(productId);
        if (!existing.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Product does not belong to this shop");
        }
        productService.deleteProduct(productId);
    }
}
