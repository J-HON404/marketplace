package com.unicam.cs.progettoweb.marketplace.notification;

import lombok.Data;

@Data
public abstract class NotificationMessage {
    private Long senderId;
    private Long recipientId;
    private String content; //non so se serve, già c'è in product e order notice!
}