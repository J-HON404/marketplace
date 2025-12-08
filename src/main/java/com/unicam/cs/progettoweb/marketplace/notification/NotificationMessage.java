package com.unicam.cs.progettoweb.marketplace.notification;

public interface NotificationMessage {
    Long getRecipientId();
    String getContent();
    String getType();
}
