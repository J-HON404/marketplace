package com.unicam.cs.progettoweb.marketplace.repository.product;

import com.unicam.cs.progettoweb.marketplace.model.notice.ProductNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductNoticeRepository extends JpaRepository<ProductNotice, Long> {
    List<ProductNotice> findByProduct_Id(Long productId);
    Optional<ProductNotice> findByIdAndProduct_Id(Long noticeId, Long productId);
}
