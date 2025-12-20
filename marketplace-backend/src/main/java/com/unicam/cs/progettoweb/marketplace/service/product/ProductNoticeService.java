package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductNoticeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductNoticeService {

    private final ProductNoticeRepository productNoticeRepository;

    public ProductNoticeService(ProductNoticeRepository productNoticeRepository) {
        this.productNoticeRepository = productNoticeRepository;
    }

    public List<ProductNotice> getProductNoticesByProductId(Long productId) {
        return productNoticeRepository.findByProduct_Id(productId);
    }

    public ProductNotice addProductNotice(ProductNotice productNotice) {
        return productNoticeRepository.save(productNotice);
    }

    public ProductNotice updateProductNotice(Long noticeId, Long productId, ProductNotice updatedNotice) {
        ProductNotice existingNotice = productNoticeRepository
                .findByIdAndProduct_Id(noticeId, productId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "ProductNotice not found with id: " + noticeId + " for productId: " + productId));
        existingNotice.setText(updatedNotice.getText());
        existingNotice.setTypeNotice(updatedNotice.getTypeNotice());
        existingNotice.setExpireDate(updatedNotice.getExpireDate());
        return productNoticeRepository.save(existingNotice);
    }


    public void deleteProductNotice(Long noticeId, Long productId) {
        ProductNotice existingNotice = productNoticeRepository.findByIdAndProduct_Id(noticeId, productId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND,
                        "productNotice not found with id: " + noticeId + " for productId: " + productId));
        productNoticeRepository.delete(existingNotice);
    }

    public ProductNotice getProductNoticeByIdAndProductId(Long noticeId, Long productId) {
        return productNoticeRepository.findByIdAndProduct_Id(noticeId, productId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND,
                        "productNotice not found with id: " + noticeId + " for productId: " + productId));
    }
}
