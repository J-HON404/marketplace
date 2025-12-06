package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductFollower;
import com.unicam.cs.progettoweb.marketplace.model.user.User;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductFollowerRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFollowerService {

    private final ProductFollowerRepository followerRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ProductFollowerService(ProductFollowerRepository followerRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.followerRepo = followerRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public ProductFollower followProduct(Long userId, Long productId) {
        return followerRepo.findByProductIdAndUserId(productId, userId)
                .orElseGet(() -> {
                    User user = userRepo.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Product product = productRepo.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    ProductFollower follower = new ProductFollower();
                    follower.setUser(user);
                    follower.setProduct(product);
                    return followerRepo.save(follower);
                });
    }

    public void unfollowProduct(Long userId, Long productId) {
        followerRepo.findByProductIdAndUserId(productId, userId)
                .ifPresent(followerRepo::delete);
    }

    public boolean isFollowing(Long userId, Long productId) {
        return followerRepo.existsByProductIdAndUserId(productId, userId);
    }

    public List<Long> getFollowedProductIds(Long userId) {
        return followerRepo.findByUserId(userId).stream()
                .map(f -> f.getProduct().getId())
                .toList();
    }

    public List<Long> getFollowerUserIds(Long productId) {
        return followerRepo.findByProductId(productId).stream()
                .map(f -> f.getUser().getId())
                .toList();
    }
}
