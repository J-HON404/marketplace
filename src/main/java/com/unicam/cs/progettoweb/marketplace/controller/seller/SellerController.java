package com.unicam.cs.progettoweb.marketplace.controller.seller;

import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.service.seller.SellerService;
import com.unicam.cs.progettoweb.marketplace.service.user.DefaultUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final DefaultUserService defaultUserService;
    private final SellerService sellerService;

    public SellerController(DefaultUserService defaultUserService, SellerService sellerService){
        this.defaultUserService = defaultUserService;
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}")
    public Seller getSellerProfile(@PathVariable Long sellerId) {
        return sellerService.getSellerById(sellerId);
    }

    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @PostMapping
    public Seller createSeller(@RequestBody Seller seller) {
        return sellerService.saveSeller(seller);
    }

    @DeleteMapping("/{sellerId}")
    public void deleteSeller(@PathVariable Long sellerId) {
        sellerService.deleteSeller(sellerId);
    }
}
