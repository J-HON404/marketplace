package com.unicam.cs.progettoweb.marketplace.events;

import com.unicam.cs.progettoweb.marketplace.model.enums.TypeOrderNotice;
import lombok.Getter;

@Getter
public class OrderEvent {
    private final Long orderId;
    private final TypeOrderNotice type;

    public OrderEvent(Long orderId, TypeOrderNotice type) {
        this.orderId = orderId;
        this.type = type;
    }

}
