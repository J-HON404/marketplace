package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.dto.product.ProductRequest;
import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShopProductService {

    private final ProductService productService;
    private final SellerShopService shopService;

    public ShopProductService(ProductService productService, SellerShopService shopService) {
        this.productService = productService;
        this.shopService = shopService;
    }

    public List<Product> getProductsOfShop(Long shopId) {
        return productService.getProductsByShopId(shopId);
    }


    public Product getProductOfShop(Long shopId, Long productId) {
        Product product = productService.getProductById(productId);
        ensureProductBelongsToShop(product, shopId);
        return product;
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
        return productService.addProduct(product);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product addProduct(Long shopId, Product product) {
        Shop shop = shopService.getShopById(shopId);
        if (product.getShop() != null && product.getShop().getId() != null &&
                !product.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "This product belongs to another shop");
        }
        if(productService.checkIfProductAlreadyExists(product.getName())){
            throw new MarketplaceException(HttpStatus.CONFLICT, "Product's name already exists");
        }
        product.setShop(shop);
        return productService.addProduct(product);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Product updateProduct(Long shopId, Long productId, Product updatedProduct) {
        Product existing = productService.getProductById(productId);
        ensureProductBelongsToShop(existing, shopId);
        return productService.updateProduct(existing.getId(), updatedProduct);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public void deleteProduct(Long shopId, Long productId) {
        Product existing = productService.getProductById(productId);
        ensureProductBelongsToShop(existing, shopId);
        productService.deleteProduct(existing.getId());
    }

    private void ensureProductBelongsToShop(Product product, Long shopId) {
        if (!product.getShop().getId().equals(shopId)) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "Product does not belong to this shop");
        }
    }
}
