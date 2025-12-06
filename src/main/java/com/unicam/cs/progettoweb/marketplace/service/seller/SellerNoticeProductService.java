package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductNoticeService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SellerNoticeProductService {

    private final ProductNoticeService productNoticeService;

    public SellerNoticeProductService(ProductNoticeService productNoticeService) {
        this.productNoticeService = productNoticeService;
    }

    public List<ProductNotice> getProductNoticesOfProductId(Long productId) {
        return productNoticeService.getProductNoticesByProductId(productId);
    }

    public ProductNotice addProductNoticeToProduct(Product product, ProductNotice notice) {
        notice.setProduct(product);
        return productNoticeService.addProductNotice(notice);
    }

    public ProductNotice updateProductNotice(Long noticeId, ProductNotice updatedNotice) {
        return productNoticeService.updateProductNotice(noticeId, updatedNotice);
    }

    public void deleteProductNotice(Long noticeId) {
        productNoticeService.deleteProductNotice(noticeId);
    }

    public ProductNotice getProductNoticeById(Long noticeId) {
        return productNoticeService.getProductNoticeById(noticeId);
    }

}
