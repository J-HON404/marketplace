package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.repository.seller.SellerRepository;
import com.unicam.cs.progettoweb.marketplace.service.order.OrderService;
import com.unicam.cs.progettoweb.marketplace.service.product.ProductService;
import com.unicam.cs.progettoweb.marketplace.service.user.DefaultUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private final DefaultUserService defaultUserService;
    private final SellerRepository sellerRepository;
    private final ProductService productService;
    private final OrderService orderService;


    public SellerService(DefaultUserService defaultUserService, SellerRepository sellerRepository, ProductService productService, OrderService orderService) {
        this.defaultUserService = defaultUserService;
        this.sellerRepository = sellerRepository;
        this.productService = productService;
        this.orderService = orderService;
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
    }

    public Seller getSellerByShopId(Long shopId) {
        return sellerRepository.findByShopId(shopId)
                .orElseThrow(() -> new RuntimeException("Seller not found for shopId: " + shopId));
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long id) {
        if (!sellerRepository.existsById(id)) {
            throw new RuntimeException("Seller not found with id: " + id);
        }
        sellerRepository.deleteById(id);
    }

}
