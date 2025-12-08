package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFollowerRepository extends JpaRepository<ProductFollower,Long> {
    Optional<ProductFollower> findByProduct_IdAndUser_Id(Long productId, Long userId);
    List<ProductFollower> findByProduct_Id(Long productId);
    List<ProductFollower> findByUser_Id(Long userId);
    boolean existsByProduct_IdAndUser_Id(Long productId, Long userId);
}
