package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.repository.customer.CustomerRepository;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductFollowerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerFollowProductService {
    private final CustomerRepository customerRepository;
    private final ProductFollowerService productFollowerService;

    public CustomerFollowProductService(CustomerRepository customerRepository, ProductFollowerService productFollowerService) {
        this.customerRepository = customerRepository;
        this.productFollowerService=productFollowerService;
    }

    private void checkCustomerExists(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
    }


    public List<Long> getFollowedProducts(Long customerId){
        checkCustomerExists(customerId);
        return productFollowerService.getFollowedProductIds(customerId);
    }

    public void followProduct(Long customerId, Long productId){
         checkCustomerExists(customerId);
        productFollowerService.followProduct(customerId,productId);
    }

    public void unfollowProduct(Long customerId, Long productId) {
        checkCustomerExists(customerId);
        productFollowerService.unfollowProduct(customerId, productId);
    }

    public boolean isFollowing(Long customerId, Long productId) {
        checkCustomerExists(customerId);
        return productFollowerService.isFollowing(customerId, productId);
    }

    // if following → unfollow, otherwise → follow
    public void toggleFollow(Long customerId, Long productId) {
        checkCustomerExists(customerId);
        if (productFollowerService.isFollowing(customerId, productId)) {
            productFollowerService.unfollowProduct(customerId, productId);
        } else {
            productFollowerService.followProduct(customerId, productId);
        }
    }

}
