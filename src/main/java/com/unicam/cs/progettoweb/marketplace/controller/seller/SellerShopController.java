package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/{sellerId}/shops")
public class SellerShopController {

    private final SellerShopService sellerShopService;

    public SellerShopController(SellerShopService sellerShopService) {
        this.sellerShopService = sellerShopService;
    }

    @GetMapping
    public List<Shop> getShopsOfSeller(@PathVariable Long sellerId) {
        return sellerShopService.getShopsBySellerId(sellerId);
    }

    @GetMapping("/{shopId}")
    public Shop getShop(@PathVariable Long shopId) {
        return sellerShopService.getShopByIdForSeller(shopId);
    }

    @PostMapping
    public Shop createShop(@PathVariable Long sellerId, @RequestBody Shop shop) {
        return sellerShopService.createShopForSeller(sellerId, shop);
    }

    @PutMapping("/{shopId}")
    public Shop updateShop(@PathVariable Long shopId, @RequestBody Shop shop) {
        return sellerShopService.updateShopForSeller(shopId, shop);
    }

    @DeleteMapping("/{shopId}")
    public void deleteShop(@PathVariable Long shopId) {
        sellerShopService.deleteShopForSeller(shopId);
    }
}
