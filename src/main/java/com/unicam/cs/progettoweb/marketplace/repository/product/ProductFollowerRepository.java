package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFollowerRepository extends JpaRepository<ProductFollower,Long> {
    Optional<ProductFollower> findByProductIdAndUserId(Long productId, Long userId);
    List<ProductFollower> findByProductId(Long productId);
    List<ProductFollower> findByUserId(Long userId);
    boolean existsByProductIdAndUserId(Long productId, Long userId);
}
