package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductFollower;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductFollowerRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.repository.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductFollowerService {

    private final ProductFollowerRepository followerRepository;
    private final ProductRepository productRepository;
    private final AccountRepository userRepository;

    public ProductFollowerService(ProductFollowerRepository followerRepository, ProductRepository productRepository, AccountRepository userRepository) {
        this.followerRepository = followerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductFollower followProduct(Long userId, Long productId) {
        return followerRepository.findByProductIdAndUserId(productId, userId)
                .orElseGet(() -> {
                    Profile user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    ProductFollower follower = new ProductFollower();
                    follower.setUser(user);
                    follower.setProduct(product);
                    return followerRepository.save(follower);
                });
    }

    public void unfollowProduct(Long userId, Long productId) {
        Optional<ProductFollower> productFollower = followerRepository.findByProductIdAndUserId(productId, userId);
        productFollower.ifPresent(followerRepository::delete);
    }

    public boolean isFollowing(Long userId, Long productId) {
        return followerRepository.existsByProductIdAndUserId(productId, userId);
    }

    public List<Long> getFollowedProductIds(Long userId) {
        return followerRepository.findByUserId(userId).stream()
                .map(f -> f.getProduct().getId())
                .toList();
    }

    public List<Long> getFollowerUserIds(Long productId) {
        return followerRepository.findByProductId(productId).stream()
                .map(f -> f.getUser().getId())
                .toList();
    }
}
