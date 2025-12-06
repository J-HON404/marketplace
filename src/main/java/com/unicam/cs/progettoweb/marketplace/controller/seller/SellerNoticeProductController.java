package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerNoticeProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/{sellerId}/noticeProducts")
public class SellerNoticeProductController {

    private final SellerNoticeProductService sellerNoticeProductService;
    private final ProductService productService;

    public SellerNoticeProductController(SellerNoticeProductService sellerNoticeProductService,
                                         ProductService productService) {
        this.sellerNoticeProductService = sellerNoticeProductService;
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public List<ProductNotice> getProductNotices(@PathVariable Long productId) {
        return sellerNoticeProductService.getProductNoticesOfProductId(productId);
    }

    @PostMapping("/{productId}")
    public ProductNotice addProductNotice(@PathVariable Long productId,
                                          @RequestBody ProductNotice notice) {
        Product product = productService.getProductById(productId);
        return sellerNoticeProductService.addProductNoticeToProduct(product, notice);
    }

    @PutMapping("/{productId}/{noticeId}")
    public ProductNotice updateProductNotice(@PathVariable Long noticeId,
                                             @RequestBody ProductNotice updatedNotice) {
        return sellerNoticeProductService.updateProductNotice(noticeId, updatedNotice);
    }

    @DeleteMapping("/{productId}/{noticeId}")
    public void deleteProductNotice(@PathVariable Long noticeId) {
        sellerNoticeProductService.deleteProductNotice(noticeId);
    }

    @GetMapping("/{productId}/notice/{noticeId}")
    public ProductNotice getProductNoticeById(@PathVariable Long noticeId) {
        return sellerNoticeProductService.getProductNoticeById(noticeId);
    }
}
