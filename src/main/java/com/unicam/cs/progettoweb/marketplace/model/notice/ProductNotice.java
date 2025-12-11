package com.unicam.cs.progettoweb.marketplace.model.product;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeProductNotice;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product_notices")
public class ProductNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column
    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeProductNotice typeNotice;

    @ManyToOne
    @JoinColumn(name = "product_id") //fk verso product_id
    private Product product;


}
