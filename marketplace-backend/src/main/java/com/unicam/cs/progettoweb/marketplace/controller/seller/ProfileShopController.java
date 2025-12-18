package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.profile.SellerShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles/{profileId}")
public class ProfileShopController {

    private final SellerShopService sellerShopService;

    public ProfileShopController(SellerShopService sellerShopService) {
        this.sellerShopService = sellerShopService;
    }


    @GetMapping("/shop")
    public ResponseEntity<ApiResponse<Shop>> getShop(@PathVariable Long profileId) {
        Shop shop = sellerShopService.getShopOfProfile(profileId);
        return ResponseEntity.ok(ApiResponse.success(shop));
    }


    @GetMapping("/shops")
    public ResponseEntity<ApiResponse<List<Shop>>> getAllShops(@PathVariable Long profileId){
        List<Shop> shops = sellerShopService.getAllShops(profileId);
        return ResponseEntity.ok(ApiResponse.success(shops));
    }


    @PostMapping("/shop")
    public ResponseEntity<ApiResponse<Shop>> createShop(@PathVariable Long profileId, @RequestParam String name) {
        Shop shop = sellerShopService.createShop(profileId, name);
        return ResponseEntity.ok(ApiResponse.success(shop));
    }


    @PostMapping("/shop/assign")
    public ResponseEntity<ApiResponse<Shop>> assignShop(@PathVariable Long profileId, @RequestBody Shop shop) {
        Shop created = sellerShopService.assignShop(profileId, shop);
        return ResponseEntity.ok(ApiResponse.success(created));
    }


    @PutMapping("/shop")
    public ResponseEntity<ApiResponse<Shop>> updateShop(@PathVariable Long profileId, @RequestBody Shop shop) {
        Shop updated = sellerShopService.updateShop(profileId, shop);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }


    @DeleteMapping("/shop")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long profileId) {
        sellerShopService.deleteShop(profileId);
        return ResponseEntity.noContent().build();
    }
}
