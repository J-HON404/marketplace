package com.unicam.cs.progettoweb.marketplace.notification;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;
import com.unicam.cs.progettoweb.marketplace.notification.channel.NotificationChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final List<NotificationChannel> channels;

    @Autowired
    public NotificationService(List<NotificationChannel> channels) {
        this.channels = channels;
    }

    public void notifyNotice(OrderNotice notice) {
        NotificationMessage message = new OrderNoticeMessage(notice);
        for (NotificationChannel channel : channels) {
            channel.sendNotification(message);
        }
    }
}

