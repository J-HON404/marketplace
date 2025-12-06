package com.unicam.cs.progettoweb.marketplace.service.user;

import com.unicam.cs.progettoweb.marketplace.model.Product;
import com.unicam.cs.progettoweb.marketplace.model.Shop;
import com.unicam.cs.progettoweb.marketplace.model.User;
import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import com.unicam.cs.progettoweb.marketplace.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements BaseUserService{

    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getProfile(Long id) {
        return null;
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

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

}