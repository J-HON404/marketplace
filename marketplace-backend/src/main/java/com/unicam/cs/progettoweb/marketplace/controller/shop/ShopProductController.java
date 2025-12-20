package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.dto.ProductRequest;
import com.unicam.cs.progettoweb.marketplace.model.Product;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopProductService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<Product>>> getProductsOfShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.getProductsOfShop(shopId)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductOfShop(@PathVariable Long shopId, @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.getProductOfShop(shopId, productId)));
    }

    @GetMapping("/not-available")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsOfShopNotAvailable(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.getProductsOfShopNotAvailable(shopId)));
    }

    @GetMapping("/future")
    public ResponseEntity<ApiResponse<List<Product>>> getFutureAvailableProducts(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.getFutureAvailableProducts(shopId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@PathVariable Long shopId, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.createProduct(shopId, productRequest)));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Product>> addProduct(@PathVariable Long shopId, @RequestBody Product product) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.addProduct(shopId, product)));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long shopId, @PathVariable Long productId, @RequestBody Product product) {
        return ResponseEntity.ok(ApiResponse.success(shopProductService.updateProduct(shopId, productId, product)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long shopId, @PathVariable Long productId) {
        shopProductService.deleteProduct(shopId, productId);
        return ResponseEntity.noContent().build();
    }
}


