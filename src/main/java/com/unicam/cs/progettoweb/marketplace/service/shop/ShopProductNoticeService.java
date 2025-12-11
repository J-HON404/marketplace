package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductNoticeService {

    private final ProductNoticeService productNoticeService;
    private final ShopProductService shopProductService;

    public ShopProductNoticeService(ProductNoticeService productNoticeService, ShopProductService shopProductService) {
        this.productNoticeService = productNoticeService;
        this.shopProductService = shopProductService;
    }

    public List<ProductNotice> getProductNoticesOfProductId(Long productId) {
        return productNoticeService.getProductNoticesByProductId(productId);
    }

    public ProductNotice addProductNoticeToProduct(Long shopId, ProductNotice notice) {
        // Qui verifichiamo che il prodotto appartenga allo shop prima di aggiungere la notice
        shopProductService.authorizeProductBelongsToShop(shopId, notice.getProduct());
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
