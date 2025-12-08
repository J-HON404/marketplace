package com.unicam.cs.progettoweb.marketplace.notification.channel;

import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;

public interface NotificationChannel {
    void sendNotification(NotificationMessage notice);
}
