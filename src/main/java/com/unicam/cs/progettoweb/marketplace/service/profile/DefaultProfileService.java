package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.repository.account.AccountRepository;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderNoticeService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.shop.ShopService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProfileService implements ProfileService {

    private final AccountRepository accountRepository;
    private final ShopService shopService;
    private final ProductService productService;
    private final OrderNoticeService orderNoticeService;

    public DefaultProfileService(AccountRepository accountRepository, ShopService shopService, ProductService productService, OrderNoticeService orderNoticeService) {
        this.accountRepository = accountRepository;
        this.shopService = shopService;
        this.productService = productService;
        this.orderNoticeService = orderNoticeService;
    }

    @Override
    public Profile findProfileById(Long profileId) {
        return accountRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
    }

    @Override
    public Profile updateProfile(Long profileId, Profile updatedProfile) {
        Profile profile = findProfileById(profileId);
        profile.setUsername(updatedProfile.getUsername());
        profile.setEmail(updatedProfile.getEmail());
        profile.setPassword(updatedProfile.getPassword());
        profile.setAddress(updatedProfile.getAddress());
        return accountRepository.save(profile);
    }

    @Override
    public Profile createProfile(Profile profile) {
        return accountRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long profileId) {
        if (!accountRepository.existsById(profileId)) {
            throw new RuntimeException("Profile not found with id: " + profileId);
        }
        accountRepository.deleteById(profileId);
    }

    @Override
    public List<OrderNotice> findOrdersAndNoticesByProfile(Long profileId) {
        return orderNoticeService.findAllOrdersAndNoticesOfProfile(profileId);
    }

    @Override
    public List<Shop> findAllShops() {
        return shopService.getAllShops();
    }

    @Override
    public List<Product> findAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public String getProfileAddress(Long profileId) {
        Profile profile = findProfileById(profileId);
        return profile.getAddress();
    }
}
