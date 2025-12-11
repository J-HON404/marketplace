package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.repository.shop.ShopRepository;
import com.unicam.cs.progettoweb.marketplace.repository.seller.SellerRepository;
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

    public Shop getShopByIdForSeller(Long sellerId, Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + shopId));
        verifyShopBelongsToSeller(sellerId, shop);
        return shop;
    }

    public Shop createShopForSeller(Long sellerId, Shop shop) {
        Seller seller = verifySellerExists(sellerId);
        shop.setSeller(seller);
        return shopRepository.save(shop);
    }

    public Shop updateShopForSeller(Long sellerId, Long shopId, Shop updatedShop) {
        Shop existingShop = getShopByIdForSeller(sellerId, shopId);
        existingShop.setName(updatedShop.getName());
        return shopRepository.save(existingShop);
    }

    public void deleteShopForSeller(Long sellerId, Long shopId) {
        Shop existingShop = getShopByIdForSeller(sellerId, shopId);
        shopRepository.delete(existingShop);
    }

    private Seller verifySellerExists(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
    }

    private void verifyShopBelongsToSeller(Long sellerId, Shop shop) {
        if (!shop.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("This shop does not belong to the seller");
        }
    }
}
