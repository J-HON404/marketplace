package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{profileId}/shop")
public class ProfileShopController {

    private final ProfileShopService sellerShopService;

    public ProfileShopController(ProfileShopService sellerShopService) {
        this.sellerShopService = sellerShopService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Shop>> getShop(@PathVariable Long profileId) {
        Shop shop = sellerShopService.getShopOfProfile(profileId);
        return ResponseEntity.ok(ApiResponse.success(shop));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Shop>> createShop(@PathVariable Long profileId, @RequestBody Shop shop) {
        Shop created = sellerShopService.createShop(profileId, shop);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Shop>> updateShop(@PathVariable Long profileId, @RequestBody Shop shop) {
        Shop updated = sellerShopService.updateShop(profileId, shop);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long profileId) {
        sellerShopService.deleteShop(profileId);
        return ResponseEntity.noContent().build();
    }
}


