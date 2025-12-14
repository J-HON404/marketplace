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

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public List<ProductNotice> getProductNoticesOfProductId(Long productId) {
        return productNoticeService.getProductNoticesByProductId(productId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public ProductNotice getProductNoticeByIdAndProductId(Long noticeId, Long productId) {
        return productNoticeService.getProductNoticeByIdAndProductId(noticeId, productId);
    }


    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public ProductNotice addProductNoticeToProduct(Long productId, ProductNotice notice) {
        notice.setProduct(productService.getProductById(productId));
        return productNoticeService.addProductNotice(notice);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public ProductNotice updateProductNotice(Long noticeId, Long productId, ProductNotice updatedNotice) {
        return productNoticeService.updateProductNotice(noticeId, productId, updatedNotice);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public void deleteProductNotice(Long noticeId, Long productId) {
        productNoticeService.deleteProductNotice(noticeId, productId);
    }

}
