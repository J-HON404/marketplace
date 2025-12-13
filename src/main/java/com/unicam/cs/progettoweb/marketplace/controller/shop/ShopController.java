package com.unicam.cs.progettoweb.marketplace.controller.shop;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Shop>>> getAllShops() {
        return ResponseEntity.ok(ApiResponse.success(shopService.getAllShops()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Shop>> getShop(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(shopService.getShopById(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<Shop>> getShopByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(shopService.getShopByName(name)));
    }

    @GetMapping("/by-seller")
    public ResponseEntity<ApiResponse<List<Shop>>> getShopBySellerId(@RequestParam Long sellerId) {
        return ResponseEntity.ok(ApiResponse.success(shopService.getShopsBySellerId(sellerId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Shop>> createShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(ApiResponse.success(shopService.addShop(shop)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Shop>> updateShop(@PathVariable Long id, @RequestBody Shop shopDetails) {
        return ResponseEntity.ok(ApiResponse.success(shopService.updateShop(id, shopDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }
}

