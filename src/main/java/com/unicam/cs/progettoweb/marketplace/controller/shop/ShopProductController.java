package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shops/{shopId}/products")
public class ShopProductController {

    private final ShopProductService shopProductService;

    public ShopProductController(ShopProductService shopProductService) {
        this.shopProductService = shopProductService;
    }

    @GetMapping
    public List<Product> getProductsOfShop(@PathVariable Long shopId) {
        return shopProductService.getProductsOfShop(shopId);
    }

    @GetMapping("/not-available")
    public List<Product> getProductsOfShopNotAvailable(@PathVariable Long shopId) {
        return shopProductService.getProductsOfShopNotAvailable(shopId);
    }

    @GetMapping("/future")
    public List<Product> getFutureAvailableProducts(@PathVariable Long shopId) {
        return shopProductService.getFutureAvailableProducts(shopId);
    }

    @PostMapping
    public Product addProduct(@PathVariable Long shopId, @RequestBody Product product) {
        return shopProductService.createProduct(shopId, product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable Long shopId, @PathVariable Long productId, @RequestBody Product product) {
        return shopProductService.updateProduct(shopId, productId, product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long shopId, @PathVariable Long productId) {
        shopProductService.deleteProduct(shopId, productId);
    }
}
