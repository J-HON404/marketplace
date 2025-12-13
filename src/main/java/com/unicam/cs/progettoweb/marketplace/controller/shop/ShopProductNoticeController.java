package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.product.ProductNotice;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopProductNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/notices")
public class ShopProductNoticeController {

    private final ShopProductNoticeService shopProductNoticeService;

    public ShopProductNoticeController(ShopProductNoticeService shopProductNoticeService) {
        this.shopProductNoticeService = shopProductNoticeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductNotice>>> getProductNotices(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductNoticeService.getProductNoticesOfProductId(productId)));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<ProductNotice>> getProductNoticeById(@PathVariable Long productId, @PathVariable Long noticeId) {
        return ResponseEntity.ok(ApiResponse.success(shopProductNoticeService.getProductNoticeByIdAndProductId(noticeId,productId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductNotice>> addProductNotice(@PathVariable Long productId, @RequestBody ProductNotice notice) {
        return ResponseEntity.ok(ApiResponse.success(shopProductNoticeService.addProductNoticeToProduct(productId, notice)));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<ProductNotice>> updateProductNotice(@PathVariable Long productId,@PathVariable Long noticeId, @RequestBody ProductNotice updatedNotice) {
        return ResponseEntity.ok(ApiResponse.success(shopProductNoticeService.updateProductNotice(noticeId,productId,updatedNotice)));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteProductNotice(@PathVariable Long productId, @PathVariable Long noticeId) {
        shopProductNoticeService.deleteProductNotice(noticeId, productId);
        return ResponseEntity.noContent().build();
    }
}
