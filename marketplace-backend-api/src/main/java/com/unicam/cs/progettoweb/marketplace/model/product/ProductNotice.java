package com.unicam.cs.progettoweb.marketplace.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicam.cs.progettoweb.marketplace.model.enums.TypeProductNotice;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Rappresenta una notifica associata a un prodotto.
 * Contiene il testo della notifica, il tipo e una data di scadenza informativa.
 */

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
    //è solo a titolo informativo, indica fino a quando è valida una promozione!

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeProductNotice typeNotice;

    @ManyToOne
    @JoinColumn(name = "product_id") //fk verso product_id
    @JsonIgnore
    private Product product;


}
