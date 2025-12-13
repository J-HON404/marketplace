package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductNoticeRepository extends JpaRepository<ProductNotice, Long> {
    List<ProductNotice> findByProduct_Id(Long productId);
}
