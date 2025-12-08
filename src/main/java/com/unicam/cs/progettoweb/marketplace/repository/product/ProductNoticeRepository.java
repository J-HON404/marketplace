package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductNoticeRepository extends JpaRepository<ProductNotice,Long> {
    Optional<ProductNotice> findByProduct_Id(Long productId);
}
