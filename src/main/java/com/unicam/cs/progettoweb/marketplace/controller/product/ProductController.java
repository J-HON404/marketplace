package com.unicam.cs.progettoweb.marketplace.controller.product;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @GetMapping("/by-shop/{shopId}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByShopId(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByShopId(shopId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product) {
        Product saved = productService.addProduct(product);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Product updated = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-shop/{shopId}/not-available")
    public ResponseEntity<ApiResponse<List<Product>>> getUnavailableProducts(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getUnavailableProductsByShop(productService.getProductById(shopId).getShop())));
    }

    @GetMapping("/by-shop/{shopId}/future")
    public ResponseEntity<ApiResponse<List<Product>>> getFutureProducts(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getFutureProductsByShop(productService.getProductById(shopId).getShop(), java.time.LocalDate.now())));
    }
}
