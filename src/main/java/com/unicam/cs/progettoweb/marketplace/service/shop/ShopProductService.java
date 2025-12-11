package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopProductService {

    private final ProductService productService;
    private final ShopService shopService;

    public ShopProductService(ProductService productService, ShopService shopService) {
        this.productService = productService;
        this.shopService = shopService;
    }

    public List<Product> getProductsOfShop(Long sellerId, Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        authorizeSellerForShop(sellerId, shop);
        return productService.getProductsByShopId(shopId);
    }

    public Product createProduct(Long sellerId, Long shopId, Product product) {
        Shop shop = shopService.getShopById(shopId);
        authorizeSellerForShop(sellerId, shop);
        if (product.getShop() != null && product.getShop().getId() != null &&
                !product.getShop().getId().equals(shopId)) {
            throw new RuntimeException("This product belongs to another shop");
        }
        product.setShop(shop);
        return productService.addProduct(product);
    }

    public Product updateProduct(Long sellerId, Long shopId, Long productId, Product updatedProduct) {
        Shop shop = shopService.getShopById(shopId);
        authorizeSellerForShop(sellerId, shop);
        Product existing = productService.getProductById(productId);
        authorizeProductBelongsToShop(shopId, existing);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setAvailabilityDate(updatedProduct.getAvailabilityDate());
        return productService.updateProduct(productId, existing);
    }

    public void deleteProduct(Long sellerId, Long shopId, Long productId) {
        Shop shop = shopService.getShopById(shopId);
        authorizeSellerForShop(sellerId, shop);
        Product existing = productService.getProductById(productId);
        authorizeProductBelongsToShop(shopId, existing);
        productService.deleteProduct(productId);
    }

    private void authorizeSellerForShop(Long sellerId, Shop shop) {
        if (!shop.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Seller not authorized for this shop");
        }
    }

    public void authorizeProductBelongsToShop(Long shopId, Product product) {
        if (product.getShop() == null || !product.getShop().getId().equals(shopId)) {
            throw new RuntimeException("Product does not belong to the shop");
        }
    }

}
