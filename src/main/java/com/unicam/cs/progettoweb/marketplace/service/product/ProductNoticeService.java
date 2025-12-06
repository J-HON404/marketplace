package com.unicam.cs.progettoweb.marketplace.service.product;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.repository.product.ProductNoticeRepository;
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
        return productNoticeRepository.findAll().stream()
                .filter(notice -> notice.getProduct().equals(productId))
                .toList();
    }

    public ProductNotice getProductNoticeById(Long id) {
        return productNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductNotice not found with id: " + id));
    }

    public ProductNotice addProductNotice(ProductNotice productNotice) {
        return productNoticeRepository.save(productNotice);
    }

    public ProductNotice updateProductNotice(Long id, ProductNotice updatedNotice) {
        ProductNotice existingNotice = getProductNoticeById(id);
        existingNotice.setText(updatedNotice.getText());
        existingNotice.setTypeNotice(updatedNotice.getTypeNotice());
        existingNotice.setExpireDate(updatedNotice.getExpireDate());
        existingNotice.setProduct(updatedNotice.getProduct());
        return productNoticeRepository.save(existingNotice);
    }

    public void deleteProductNotice(Long id) {
        ProductNotice existingNotice = getProductNoticeById(id);
        productNoticeRepository.delete(existingNotice);
    }

}
