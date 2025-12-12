package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopProductNoticeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shops/{shopId}/products/{productId}/notices")
public class ShopProductNoticeController {

    private final ShopProductNoticeService shopProductNoticeService;
    private final ProductService productService;

    public ShopProductNoticeController(ShopProductNoticeService shopProductNoticeService, ProductService productService) {
        this.shopProductNoticeService = shopProductNoticeService;
        this.productService = productService;
    }

    @GetMapping
    public List<ProductNotice> getProductNotices(@PathVariable Long productId) {
        return shopProductNoticeService.getProductNoticesOfProductId(productId);
    }

    @GetMapping("/{noticeId}")
    public ProductNotice getProductNoticeById(@PathVariable Long noticeId) {
        return shopProductNoticeService.getProductNoticeById(noticeId);
    }

    @PostMapping
    public ProductNotice addProductNotice(@PathVariable Long productId,@RequestBody ProductNotice notice) {
        return shopProductNoticeService.addProductNoticeToProduct(productId, notice);
    }

    @PutMapping("/{noticeId}")
    public ProductNotice updateProductNotice(@PathVariable Long noticeId, @RequestBody ProductNotice updatedNotice) {
        return shopProductNoticeService.updateProductNotice(noticeId, updatedNotice);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteProductNotice(@PathVariable Long noticeId) {
        shopProductNoticeService.deleteProductNotice(noticeId);
    }
}
