package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.order.Order;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles/{profileId}/orders")
public class ProfileOrderHistoryController {

    private final ProfileOrderService customerOrderService;

    public ProfileOrderHistoryController(ProfileOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getOrders(@PathVariable Long profileId) {
        List<Order> orders = customerOrderService.getOrdersOfProfile(profileId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }


    @PutMapping("/{orderId}/confirm-delivered")
    public ResponseEntity<ApiResponse<Void>> confirmDelivered(@PathVariable Long profileId, @PathVariable Long orderId) {
        customerOrderService.confirmDelivered(profileId, orderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
