package com.unicam.cs.progettoweb.marketplace.service.shop;

import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.ShopRepository;
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

    public Shop getShopById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    public Shop addShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long id, Shop shopDetails) {

        Shop shop = getShopById(id);

        shop.setName(shopDetails.getName());
        shop.setSeller(shopDetails.getSeller());

        return shopRepository.save(shop);
    }

    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
