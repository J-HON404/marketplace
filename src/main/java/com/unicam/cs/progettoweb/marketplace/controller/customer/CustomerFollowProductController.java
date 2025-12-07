package com.unicam.cs.progettoweb.marketplace.controller.customer;

import com.unicam.cs.progettoweb.marketplace.service.customer.CustomerFollowProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/follows")
public class CustomerFollowProductController {

    private final CustomerFollowProductService followService;

    public CustomerFollowProductController(CustomerFollowProductService followService) {
        this.followService = followService;
    }

    @PostMapping("/{productId}")
    public void follow(@PathVariable Long customerId, @PathVariable Long productId) {
        followService.followProduct(customerId, productId);
    }

    @DeleteMapping("/{productId}")
    public void unfollow(@PathVariable Long customerId, @PathVariable Long productId) {
        followService.unfollowProduct(customerId, productId);
    }

    @GetMapping
    public List<Long> getFollowed(@PathVariable Long customerId) {
        return followService.getFollowedProducts(customerId);
    }

    @GetMapping("/{productId}")
    public boolean isFollowing(@PathVariable Long customerId, @PathVariable Long productId) {
        return followService.isFollowing(customerId, productId);
    }

    @PostMapping("/{productId}/toggle")
    public void toggle(@PathVariable Long customerId, @PathVariable Long productId) {
        followService.toggleFollow(customerId, productId);
    }
}