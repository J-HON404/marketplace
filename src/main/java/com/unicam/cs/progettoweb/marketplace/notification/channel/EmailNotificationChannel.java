package com.unicam.cs.progettoweb.marketplace.notification.channel;

import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationChannel implements NotificationChannel {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendNotification(NotificationMessage message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(getEmailFromUserId(message.getRecipientId()));
        mail.setSubject("Marketplace Notification");
        mail.setText(message.getContent());
        mailSender.send(mail);
    }

    private String getEmailFromUserId(Long userId) {
        // TODO
        return "";
    }
}



