package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ShopRequest;
import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.enums.ShopCategory;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.ShopRepository;
import com.unicam.cs.progettoweb.marketplace.security.ShopSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SellerShopService {

    private final ShopRepository shopRepository;
    private final ShopSecurity shopSecurity;
    private final ProfileService profileService;

    public SellerShopService(ShopRepository shopRepository,ShopSecurity shopSecurity, ProfileService profileService) {
        this.shopRepository = shopRepository;
        this.shopSecurity = shopSecurity;
        this.profileService = profileService;
    }

    @PreAuthorize("hasRole('SELLER')")
    public Shop getShopOfProfile(Long profileId) {
        return shopRepository.findBySeller_Id(profileId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "Shop not found for profile id: " + profileId));
    }

    @PreAuthorize("isAuthenticated()")
    public Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new MarketplaceException(
                        HttpStatus.NOT_FOUND,
                        "Shop not found with id: " + shopId));
    }

    @PreAuthorize("isAuthenticated()")
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @PreAuthorize("hasRole('SELLER') and principal.id == #sellerId")
    public Shop createShop(Long sellerId, ShopRequest shopRequest) {
        Profile seller = validateSeller(sellerId);
        String shopName = shopRequest.getName();
        ShopCategory shopCategory = shopRequest.getCategory();
        if (shopRepository.findByName(shopRequest.getName()).isPresent()) {
            throw new MarketplaceException(HttpStatus.CONFLICT, "Shop with name " + shopName + " already exists");
        }
        if (shopCategory == null) {
            throw new MarketplaceException(HttpStatus.BAD_REQUEST, "La categoria è obbligatoria");
        }
        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setSeller(seller);
        seller.setShop(shop);
        shop.setShopCategory(shopCategory);
        return shopRepository.save(shop);
    }

    @PreAuthorize("hasRole('SELLER') and principal.id == #profileId and @shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Shop updateShop(Long profileId, Long shopId, ShopRequest updatedShop) {
        Shop existingShop = getShopById(shopId);
        if (!existingShop.getName().equals(updatedShop.getName())) {
            if (shopRepository.findByName(updatedShop.getName()).isPresent()) {
                throw new MarketplaceException(HttpStatus.CONFLICT, "Shop name already exists");
            }
            existingShop.setName(updatedShop.getName());
        }
        if (updatedShop.getCategory() != null) {
            existingShop.setShopCategory(updatedShop.getCategory());
        }
        return shopRepository.save(existingShop);
    }

    private Profile validateSeller(Long sellerId) {
        Profile seller = profileService.findProfileById(sellerId);
        if (shopRepository.existsBySeller_Id(sellerId)) {
            throw new MarketplaceException(
                    HttpStatus.BAD_REQUEST,
                    "Seller already owns a shop");
        }
        return seller;
    }

    public boolean isOwnerOfShop(Long profileId, Long shopId) {
        return shopSecurity.isSellerOfShop(profileId, shopId);
    }

}
