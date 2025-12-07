package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerProductService {

    private final ProductService productService;
    private final SellerService sellerService;

    public SellerProductService(ProductService productService, SellerService sellerService) {
        this.productService = productService;
        this.sellerService = sellerService;
    }

    public List<Product> getProductsOfSeller(Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        return productService.getProductsByShopId(seller.getShop().getId());
    }

    public Product createProduct(Long sellerId, Product product) {
        Seller seller = sellerService.getSellerById(sellerId);
        product.setShop(seller.getShop());
        return productService.addProduct(product);
    }

    public Product updateProduct(Long sellerId, Long productId, Product updatedProduct) {
        Seller seller = sellerService.getSellerById(sellerId);
        Product existing = productService.getProductById(productId);
        // un seller può modificare solo prodotti del suo shop
        if (!existing.getShop().getId().equals(seller.getShop().getId())) {
            throw new RuntimeException("Seller not allowed to modify this product");
        }
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        return productService.addProduct(existing);
    }

    public void deleteProduct(Long sellerId, Long productId) {
        Seller seller = sellerService.getSellerById(sellerId);
        Product existingProduct = productService.getProductById(productId);
        // un seller può eliminare solo prodotti del suo shop
        if (!existingProduct.getShop().getId().equals(seller.getShop().getId())) {
            throw new RuntimeException("Seller not allowed to delete this product");
        }
        productService.deleteProduct(productId);
    }
}
