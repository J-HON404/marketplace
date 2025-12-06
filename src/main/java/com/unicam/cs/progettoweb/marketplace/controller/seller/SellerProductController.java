package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductFollowerService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/{sellerId}/products")
public class SellerProductController {

    private final SellerProductService sellerProductService;
    private final ProductFollowerService productFollowerService;

    public SellerProductController(SellerProductService sellerProductService, ProductFollowerService productFollowerService) {
        this.sellerProductService = sellerProductService;
        this.productFollowerService = productFollowerService;
    }

    @GetMapping
    public List<Product> getProductsOfSeller(@PathVariable Long sellerId) {
        return sellerProductService.getProductsOfSeller(sellerId);
    }

    @GetMapping("/{productId}/followers")
    public List<Long> getFollowers (@PathVariable Long productId) {
        return productFollowerService.getFollowerUserIds(productId);
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
