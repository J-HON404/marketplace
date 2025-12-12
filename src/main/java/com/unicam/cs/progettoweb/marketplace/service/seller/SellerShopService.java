package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.repository.shop.ShopRepository;
import com.unicam.cs.progettoweb.marketplace.repository.seller.SellerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerShopService {

    private final ShopRepository shopRepository;
    private final SellerRepository sellerRepository;

    public SellerShopService(ShopRepository shopRepository, SellerRepository sellerRepository) {
        this.shopRepository = shopRepository;
        this.sellerRepository = sellerRepository;
    }

    public List<Shop> getShopsBySellerId(Long sellerId) {
        verifySellerExists(sellerId);
        return shopRepository.findBySeller_Id(sellerId);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Shop getShopByIdForSeller(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + shopId));
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public Shop updateShopForSeller(Long shopId, Shop updatedShop) {
        Shop existingShop = getShopByIdForSeller(shopId);
        existingShop.setName(updatedShop.getName());
        return shopRepository.save(existingShop);
    }

    @PreAuthorize("@shopSecurity.isSellerOfShop(principal.id, #shopId)")
    public void deleteShopForSeller(Long shopId) {
        Shop existingShop = getShopByIdForSeller(shopId);
        shopRepository.delete(existingShop);
    }

    public Shop createShopForSeller(Long sellerId, Shop shop) {
        Seller seller = verifySellerExists(sellerId);
        shop.setSeller(seller);
        return shopRepository.save(shop);
    }

    private Seller verifySellerExists(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
    }
}
