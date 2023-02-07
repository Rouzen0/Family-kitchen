package com.example.family_kitchen.model;

public class Chat {
    private String OrderId,CustomerId,FamilyId,MessageId,Message,MessageDate,MessageTime,SenderId;

    public Chat(){}

    public Chat(String orderId, String customerId, String familyId, String messageId, String message, String messageDate, String messageTime, String senderId) {
        OrderId = orderId;
        CustomerId = customerId;
        FamilyId = familyId;
        MessageId = messageId;
        Message = message;
        MessageDate = messageDate;
        MessageTime = messageTime;
        SenderId = senderId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getFamilyId() {
        return FamilyId;
    }

    public void setFamilyId(String familyId) {
        FamilyId = familyId;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageDate() {
        return MessageDate;
    }

    public void setMessageDate(String messageDate) {
        MessageDate = messageDate;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(String messageTime) {
        MessageTime = messageTime;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
}
