package com.unicam.cs.progettoweb.marketplace.model.seller;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "sellers")
@NoArgsConstructor
public class Seller extends Profile {
    @OneToOne(mappedBy = "seller") //shop ha riferimento con FK su seller
    private Shop shop;
}
