package com.unicam.cs.progettoweb.marketplace.model;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order_notices")
public class OrderNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOrderNotice typeNotice;

    @ManyToOne
    @JoinColumn(name = "order_id") //fk verso order_id
    private Order order;
}
