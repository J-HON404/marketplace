package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
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

    public List<ProductNotice> getAllProductNotices() {
        return productNoticeRepository.findAll();
    }

    public List<ProductNotice> getProductNoticesByProductId(Long productId) {
        return productNoticeRepository.findByProduct_Id(productId);
    }

    public ProductNotice getProductNoticeById(Long productNoticeId) {
        return productNoticeRepository.findById(productNoticeId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND,
                        "ProductNotice not found with id: " + productNoticeId));
    }

    public ProductNotice addProductNotice(ProductNotice productNotice) {
        return productNoticeRepository.save(productNotice);
    }

    public ProductNotice updateProductNotice(Long productNoticeId, ProductNotice updatedNotice) {
        ProductNotice existingNotice = getProductNoticeById(productNoticeId);
        existingNotice.setText(updatedNotice.getText());
        existingNotice.setTypeNotice(updatedNotice.getTypeNotice());
        existingNotice.setExpireDate(updatedNotice.getExpireDate());
        existingNotice.setProduct(updatedNotice.getProduct());
        return productNoticeRepository.save(existingNotice);
    }

    public void deleteProductNotice(Long productNoticeId) {
        ProductNotice existingNotice = getProductNoticeById(productNoticeId);
        productNoticeRepository.delete(existingNotice);
    }

}
