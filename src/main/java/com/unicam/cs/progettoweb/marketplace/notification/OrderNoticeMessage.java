package com.unicam.cs.progettoweb.marketplace.notification;

import com.unicam.cs.progettoweb.marketplace.model.order.OrderNotice;

public class OrderNoticeMessage implements NotificationMessage {

    private final Long recipientId;
    private final String content;
    private final String type;

    public OrderNoticeMessage(OrderNotice notice) {
        this.recipientId = notice.getOrder().getCustomer().getId();
        this.content = notice.getText();
        this.type = notice.getTypeNotice().name();
    }

    @Override
    public Long getRecipientId() { return recipientId; }

    @Override
    public String getContent() { return content; }

    @Override
    public String getType() { return type; }
}
