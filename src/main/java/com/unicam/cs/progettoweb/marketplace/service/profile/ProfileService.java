package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;

import java.util.List;

public interface ProfileService {

    Profile findProfileById(Long profileId);

    Profile updateProfile(Long profileId, Profile updatedProfile);

    Profile createProfile(Profile profile);

    void deleteProfile(Long profileId);

    List<OrderNotice> findOrdersAndNoticesByProfile(Long profileId);

    List<Shop> findAllShops();

    List<Product> findAllProducts();

    String getProfileAddress(Long profileId);
}
