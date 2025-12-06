package com.unicam.cs.progettoweb.marketplace.service.user;

import com.unicam.cs.progettoweb.marketplace.model.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.model.User;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements BaseUserService{
    @Override
    public User getProfile(Long id) {
        return null;
    }

    @Override
    public List<NotificationMessage> getReceivedMessages(Long userId) {
        return null;
    }

    @Override
    public List<Shop> getShopList() {
        return null;
    }

    @Override
    public List<Product> getProductsList() {
        return null;
    }
}