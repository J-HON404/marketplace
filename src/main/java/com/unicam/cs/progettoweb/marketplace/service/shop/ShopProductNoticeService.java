package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopProductNoticeService {

    private final ProductNoticeService productNoticeService;
    private final ProductService productService;

    public ShopProductNoticeService(ProductNoticeService productNoticeService, ProductService productService) {
        this.productNoticeService = productNoticeService;
        this.productService = productService;
    }

    @PreAuthorize("@shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public List<ProductNotice> getProductNoticesOfProductId(Long productId) {
        return productNoticeService.getProductNoticesByProductId(productId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public ProductNotice addProductNoticeToProduct(Long productId, ProductNotice notice) {
        notice.setProduct(productService.getProductById(productId));
        return productNoticeService.addProductNotice(notice);
    }

    @PreAuthorize("@shopSecurity.isSellerOfProductForNotice(principal.id, #noticeId)")
    public ProductNotice updateProductNotice(Long noticeId, ProductNotice updatedNotice) {
        return productNoticeService.updateProductNotice(noticeId, updatedNotice);
    }

    @PreAuthorize("@shopSecurity.isSellerOfProductForNotice(principal.id, #noticeId)")
    public void deleteProductNotice(Long noticeId) {
        productNoticeService.deleteProductNotice(noticeId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfProductForNotice(principal.id, #noticeId)")
    public ProductNotice getProductNoticeById(Long noticeId) {
        return productNoticeService.getProductNoticeById(noticeId);
    }
}
