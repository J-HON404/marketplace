package com.unicam.cs.progettoweb.marketplace.service.account;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;

import java.util.List;

public interface AccountService {

    Profile getProfile(Long id);

    Profile updateProfile(Long id, Profile updatedProfile);

    Profile createProfile(Profile profile);

    void deleteProfile(Long id);

    List<NotificationMessage> getReceivedMessages(Long userId);

    List<Shop> getShopsList();

    List<Product> getProductsList();
}
