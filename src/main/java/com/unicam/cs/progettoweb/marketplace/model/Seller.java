package com.unicam.cs.progettoweb.marketplace.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "sellers")
@NoArgsConstructor
public class Seller extends User {
    @OneToOne(mappedBy = "seller") //shop ha riferimento con FK su seller
    private Shop shop;
}
