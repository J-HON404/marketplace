package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<Seller>> getSeller(@PathVariable Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(ApiResponse.success(seller));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Seller>>> getAllSellers() {
        List<Seller> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(ApiResponse.success(sellers));
    }

    @GetMapping("/by-shopId")
    public ResponseEntity<ApiResponse<Seller>> getSellerByShopId(@RequestParam Long shopId) {
        Seller seller = sellerService.getSellerByShopId(shopId);
        return ResponseEntity.ok(ApiResponse.success(seller));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Seller>> createSeller(@RequestBody Seller seller) {
        Seller created = sellerService.saveSeller(seller);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<Void>> deleteSeller(@PathVariable Long sellerId) {
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.noContent().build();
    }
}
