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
    public List<Product> getProductsOfShop(@PathVariable Long shopId, @RequestParam Long sellerId) {
        return shopProductService.getProductsOfShop(sellerId, shopId);
    }

    @PostMapping
    public Product addProduct(@PathVariable Long shopId, @RequestParam Long sellerId, @RequestBody Product product) {
        return shopProductService.createProduct(sellerId, shopId, product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable Long shopId, @PathVariable Long productId, @RequestParam Long sellerId,
                                 @RequestBody Product product) {
        return shopProductService.updateProduct(sellerId, shopId, productId, product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long shopId, @PathVariable Long productId, @RequestParam Long sellerId) {
        shopProductService.deleteProduct(sellerId, shopId, productId);
    }
}
