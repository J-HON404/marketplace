package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.profile.ProfileRepository;
import com.unicam.cs.progettoweb.marketplace.repository.shop.ShopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class ProfileShopService {

    private final ShopRepository shopRepository;
    private final ProfileRepository profileRepository;

    public ProfileShopService(ShopRepository shopRepository, ProfileRepository profileRepository) {
        this.shopRepository = shopRepository;
        this.profileRepository = profileRepository;
    }

    public Shop getShopOfProfile(Long profileId) {
        return shopRepository.findBySeller_Id(profileId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "Shop not found for profile id: " + profileId));
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "Shop not found with id: " + shopId));
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Shop updateShop(Long shopId, Shop updatedShop) {
        Shop existingShop = getShopById(shopId);
        existingShop.setName(updatedShop.getName());
        return shopRepository.save(existingShop);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public void deleteShop(Long shopId) {
        Shop shop = getShopById(shopId);
        shopRepository.delete(shop);
    }

    public Shop createShop(Long profileId, Shop shop) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "Profile not found with id: " + profileId));

        if (shopRepository.existsBySeller_Id(profileId)) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST, "Profile already owns a shop");
        }

        shop.setSeller(profile);
        return shopRepository.save(shop);
    }
}