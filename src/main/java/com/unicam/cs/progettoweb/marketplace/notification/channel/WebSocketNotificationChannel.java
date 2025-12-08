package com.unicam.cs.progettoweb.marketplace.notification.channel;

import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationChannel implements NotificationChannel {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketNotificationChannel(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/user/" + message.getRecipientId(), message);
    }
}

