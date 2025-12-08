package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.service.profile.DefaultProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final DefaultProfileService profileService;

    public ProfileController(DefaultProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    public Profile getProfile(@PathVariable Long profileId) {
        return profileService.findProfileById(profileId);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }

    @PutMapping("/{profileId}")
    public Profile updateProfile(
            @PathVariable Long profileId,
            @RequestBody Profile updatedProfile
    ) {
        return profileService.updateProfile(profileId, updatedProfile);
    }

    @DeleteMapping("/{profileId}")
    public void deleteProfile(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
    }

    @GetMapping("/{profileId}/orders-notices")
    public List<OrderNotice> getOrdersAndNotices(@PathVariable Long profileId) {
        return profileService.findOrdersAndNoticesByProfile(profileId);
    }

    @GetMapping("/shops")
    public List<Shop> getAllShops() {
        return profileService.findAllShops();
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return profileService.findAllProducts();
    }

    @GetMapping("/{profileId}/address")
    public String getProfileAddress(@PathVariable Long profileId) {
        return profileService.getProfileAddress(profileId);
    }
}
