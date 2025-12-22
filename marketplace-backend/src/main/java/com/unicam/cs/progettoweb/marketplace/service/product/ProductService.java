package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "product not found with id: " + productId));
    }

    public List<Product> getProductsByShopId(Long shopId) {
        return productRepository.findByShop_Id(shopId);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existing = getProductById(productId);
        copyProductDetails(existing, updatedProduct);
        return productRepository.save(existing);
    }

    private void copyProductDetails(Product existing, Product updated) {
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());
        existing.setAvailabilityDate(updated.getAvailabilityDate());
    }

    public void deleteProduct(Long productId) {
        getProductById(productId);
        productRepository.deleteById(productId);
    }

    public void checkProductDateAvailability(Product product) {
        if (product.getAvailabilityDate().isAfter(LocalDate.now())) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "product '" + product.getName() + "' is not available yet. Available from: " + product.getAvailabilityDate());
        }
    }

    public void updateProductQuantity(Long productId, int newQuantity) {
        Product product = getProductById(productId);
        if (newQuantity < 0) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST, "Stock insufficiente");
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);
    }

    public boolean checkIfProductAlreadyExists(String productName){
        return productRepository.existsByName(productName);
    }

    public List<Product> getUnavailableProductsByShop(Shop shop) {
        return productRepository.findByShopAndQuantityEquals(shop, 0);
    }

    public List<Product> getFutureProductsByShop(Shop shop, LocalDate today) {
        return productRepository.findByShopAndAvailabilityDateGreaterThan(shop, today);
    }
}
