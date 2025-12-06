package com.unicam.cs.progettoweb.marketplace.service.user;

import com.unicam.cs.progettoweb.marketplace.model.product.Product;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import com.unicam.cs.progettoweb.marketplace.model.user.User;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;

import java.util.List;

public interface BaseUserService {

    User getProfile(Long id);

    List<NotificationMessage> getReceivedMessages(Long userId);

    List<Shop> getShopsList();

    List<Product> getProductsList();
}
