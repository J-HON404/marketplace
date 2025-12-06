package com.unicam.cs.progettoweb.marketplace.service.user;

import com.unicam.cs.progettoweb.marketplace.model.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.model.User;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;

import java.util.List;

public interface BaseUserService {

    User getProfile(Long id);

    List<NotificationMessage> getReceivedMessages(Long userId);

    List<Shop> getShopList();

    List<Product> getProductsList();
}
