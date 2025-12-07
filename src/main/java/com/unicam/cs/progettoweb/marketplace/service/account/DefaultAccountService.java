package com.unicam.cs.progettoweb.marketplace.service.account;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import com.unicam.cs.progettoweb.marketplace.repository.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAccountService implements AccountService {

    private final AccountRepository userRepository;

    public DefaultAccountService(AccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Profile getProfile(Long id) {
        return null;
    }

    @Override
    public Profile updateProfile(Long id, Profile updatedProfile) {
        return null;
    }

    @Override
    public Profile createProfile(Profile profile) {
        return null;
    }

    @Override
    public void deleteProfile(Long id) {

    }

    @Override
    public List<NotificationMessage> getReceivedMessages(Long userId) {
        return null;
    }

    @Override
    public List<Shop> getShopsList() {
        return null;
    }

    @Override
    public List<Product> getProductsList() {
        return null;
    }

    public Profile getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

}