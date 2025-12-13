package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerShopController {

    private final SellerShopService sellerShopService;

    public SellerShopController(SellerShopService sellerShopService) {
        this.sellerShopService = sellerShopService;
    }

    @GetMapping("/{sellerId}/shops")
    public ResponseEntity<ApiResponse<List<Shop>>> getShopsOfSeller(@PathVariable Long sellerId) {
        List<Shop> shops = sellerShopService.getShopsBySellerId(sellerId);
        return ResponseEntity.ok(ApiResponse.success(shops));
    }

    @GetMapping("/shops/{shopId}")
    public ResponseEntity<ApiResponse<Shop>> getShop(@PathVariable Long shopId) {
        Shop shop = sellerShopService.getShopByIdForSeller(shopId);
        return ResponseEntity.ok(ApiResponse.success(shop));
    }

    @PostMapping("/{sellerId}/shops")
    public ResponseEntity<ApiResponse<Shop>> createShop(@PathVariable Long sellerId, @RequestBody Shop shop) {
        Shop created = sellerShopService.createShopForSeller(sellerId, shop);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/shops/{shopId}")
    public ResponseEntity<ApiResponse<Shop>> updateShop(@PathVariable Long shopId, @RequestBody Shop shop) {
        Shop updated = sellerShopService.updateShopForSeller(shopId, shop);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/shops/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long shopId) {
        sellerShopService.deleteShopForSeller(shopId);
        return ResponseEntity.noContent().build();
    }
}

