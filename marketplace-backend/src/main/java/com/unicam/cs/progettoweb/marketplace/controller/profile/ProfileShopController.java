package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.dto.ShopRequest;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
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
    public ResponseEntity<ApiResponse<List<Shop>>> getAllShops(){
        List<Shop> shops = sellerShopService.getAllShops();
        return ResponseEntity.ok(ApiResponse.success(shops));
    }

    @GetMapping("shops/{shopId}/verify-owner")
    public ResponseEntity<ApiResponse<Boolean>> checkSellerOwnership(@PathVariable Long profileId, @PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(sellerShopService.isOwnerOfShop(profileId, shopId)));
    }


    @PostMapping("/shop")
    public ResponseEntity<ApiResponse<Shop>> createShop(@PathVariable Long profileId, @RequestBody ShopRequest shopRequest) {
        Shop shop = sellerShopService.createShop(profileId, shopRequest);
        return ResponseEntity.ok(ApiResponse.success(shop));
    }


    @PutMapping("/shop/{shopId}")
    public ResponseEntity<ApiResponse<Shop>> updateShop(@PathVariable Long profileId, @PathVariable Long shopId, @RequestBody ShopRequest shopRequest) {
        Shop updated = sellerShopService.updateShop(profileId,shopId,shopRequest);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }


    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long profileId, @PathVariable Long shopId) {
        sellerShopService.deleteShop(profileId,shopId);
        return ResponseEntity.noContent().build();
    }


}
