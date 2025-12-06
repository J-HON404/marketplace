package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/{sellerId}/products")
public class SellerProductController {

    private final SellerProductService sellerProductService;

    public SellerProductController(SellerProductService sellerProductService) {
        this.sellerProductService = sellerProductService;
    }

    @GetMapping
    public List<Product> getProductsOfSeller(@PathVariable Long sellerId) {
        return sellerProductService.getProductsOfSeller(sellerId);
    }

    @PostMapping
    public Product addProduct(@PathVariable Long sellerId, @RequestBody Product product) {
        return sellerProductService.createProduct(sellerId, product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable Long sellerId,
                                 @PathVariable Long productId,
                                 @RequestBody Product product) {
        return sellerProductService.updateProduct(sellerId, productId, product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long sellerId, @PathVariable Long productId) {
        sellerProductService.deleteProduct(sellerId, productId);
    }
}
