package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.shop.ShopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository){
        this.shopRepository = shopRepository;
    }

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "shop not found with id: " + shopId));
    }

    public Shop getShopByName(String name) {
        return shopRepository.findByName(name)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "shop not found with name: " + name));
    }

    public Shop addShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long shopId, Shop shopDetails) {
        Shop existingShop = getShopById(shopId);
        existingShop.setName(shopDetails.getName());
        existingShop.setSeller(shopDetails.getSeller());
        return shopRepository.save(existingShop);
    }

    public void deleteShop(Long shopId) {
        getShopById(shopId);
        shopRepository.deleteById(shopId);
    }

    public List<Shop> getShopsBySellerId(Long sellerId) {
        return shopRepository.findBySeller_Id(sellerId);
    }
}
