package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    public List<Product> getProductsByShopId(Long shopId) {
        return productRepository.findByShop_Id(shopId);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product productDetails) {
        Product product = getProductById(productId);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setAvailabilityDate(productDetails.getAvailabilityDate());
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    public void checkProductDateAvailability(Product product) {
        if (product.getAvailabilityDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Product '" + product.getName() + "' is not available yet. Available from: " + product.getAvailabilityDate());
        }
    }

    public List<Product> getUnavailableProductsByShop(Shop shop) {
        return productRepository.findByShopAndQuantityEquals(shop, 0);
    }

    public List<Product> getFutureProductsByShop(Shop shop, LocalDate today) {
        return productRepository.findByShopAndAvailabilityDateGreaterThan(shop, today);
    }

}
