package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductFollower;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductFollowerRepository;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductRepository;
import com.unicam.cs.progettoweb.marketplace.repository.account.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductFollowerService {

    private final ProductFollowerRepository followerRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public ProductFollowerService(ProductFollowerRepository followerRepository, ProductRepository productRepository, AccountRepository accountRepository) {
        this.followerRepository = followerRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    public ProductFollower followProduct(Long userId, Long productId) {
        return followerRepository.findByProduct_IdAndUser_Id(productId, userId)
                .orElseGet(() -> {
                    Profile user = accountRepository.findById(userId)
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
        ProductFollower productFollower = followerRepository.findByProduct_IdAndUser_Id(productId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "No follow relationship found for userId " + userId + " and productId " + productId
                ));
        followerRepository.delete(productFollower);
    }

    public boolean isFollowing(Long userId, Long productId) {
        return followerRepository.existsByProduct_IdAndUser_Id(productId, userId);
    }

    public List<Long> getFollowedProductIds(Long userId) {
        return followerRepository.findByUser_Id(userId).stream()
                .map(f -> f.getProduct().getId())
                .toList();
    }

    public List<Long> getFollowerUserIds(Long productId) {
        return followerRepository.findByProduct_Id(productId).stream()
                .map(f -> f.getUser().getId())
                .toList();
    }
}
