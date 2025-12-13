package com.unicam.cs.progettoweb.marketplace.model.account;

import com.unicam.cs.progettoweb.marketplace.model.enums.ProfileType;
import com.unicam.cs.progettoweb.marketplace.model.shop.Shop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileType type;

    @OneToOne(mappedBy = "seller")
    private Shop shop; // solo se il profilo è SELLER
}
