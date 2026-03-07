package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.dto.product.ProductNoticeRequest;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servizio per la gestione dei ProductNotices (avvisi) dei prodotti di uno shop
 * Questo servizio consente a venditori di:
 * - Creare nuovi ProductNotice per un prodotto
 * - Aggiornare ProductNotice esistenti
 * - Eliminare ProductNotice
 * - Recuperare tutti i ProductNotice di un prodotto o uno specifico per ID
 */

@Transactional
@Service
public class ShopProductNoticeService {

    private final ProductNoticeService productNoticeService;
    private final ProductService productService;

    public ShopProductNoticeService(ProductNoticeService productNoticeService, ProductService productService) {
        this.productNoticeService = productNoticeService;
        this.productService = productService;
    }

    @PreAuthorize("isAuthenticated()")
    public List<ProductNotice> getProductNoticesOfProduct(Long productId) {
        return productNoticeService.getProductNoticesByProductId(productId);
    }

    @PreAuthorize("isAuthenticated()")
    public ProductNotice getProductNoticeByIdAndProductId(Long noticeId, Long productId) {
        return productNoticeService.getProductNoticeByIdAndProductId(noticeId, productId);
    }

    @PreAuthorize("hasRole('SELLER') and @shopSecurity.isSellerOfProduct(principal.id, #productId)")
    public ProductNotice createProductNotice(Long productId, ProductNoticeRequest productNoticeRequest) {
        ProductNotice notice = new ProductNotice();
        notice.setText(productNoticeRequest.text);
        notice.setExpireDate(productNoticeRequest.expireDate);
        notice.setTypeNotice(productNoticeRequest.type);
        notice.setProduct(productService.getProductById(productId));
        return productNoticeService.addProductNotice(notice);
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
