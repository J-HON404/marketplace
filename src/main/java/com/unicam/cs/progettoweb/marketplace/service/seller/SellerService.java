package com.unicam.cs.progettoweb.marketplace.service.seller;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.seller.Seller;
import com.unicam.cs.progettoweb.marketplace.repository.seller.SellerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND,"seller not found with id: " + sellerId));
    }

    public Seller getSellerByShopId(Long shopId) {
        return sellerRepository.findByShop_Id(shopId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "Seller not found for shopId: " + shopId));
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long sellerId) {
        getSellerById(sellerId);
        sellerRepository.deleteById(sellerId);
    }
}