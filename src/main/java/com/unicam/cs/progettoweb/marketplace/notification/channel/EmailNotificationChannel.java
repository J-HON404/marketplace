package com.unicam.cs.progettoweb.marketplace.notification.channel;

import com.unicam.cs.progettoweb.marketplace.notification.NotificationMessage;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;
    private final ProfileService accountService;

    @Autowired
    public EmailNotificationChannel(JavaMailSender mailSender, ProfileService accountService) {
        this.mailSender = mailSender;
        this.accountService = accountService;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        String recipientEmail = accountService.findProfileById(message.getRecipientId()).getEmail();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipientEmail);
        mail.setSubject("Marketplace Notification");
        mail.setText(message.getContent());
        mailSender.send(mail);
    }
}





