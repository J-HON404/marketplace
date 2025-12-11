package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.shop.ShopRepository;
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
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + shopId));
    }

    public Shop addShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long shopId, Shop shopDetails) {
        Shop shop = getShopById(shopId);
        shop.setName(shopDetails.getName());
        shop.setSeller(shopDetails.getSeller());
        return shopRepository.save(shop);
    }

    public void deleteShop(Long shopId) {
        if (!shopRepository.existsById(shopId)) {
            throw new RuntimeException("Shop not found with id: " + shopId);
        }
        shopRepository.deleteById(shopId);
    }

    public List<Shop> getShopsBySellerId(Long sellerId) {
        return shopRepository.findBySeller_Id(sellerId);
    }

    public Shop getShopByName(String name) {
        return shopRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Shop not found with name: " + name));
    }
}
