package com.example.chat.Chat;

public class MessageObject {
    private String messageId,senderId,message;

    public MessageObject(String messageId,String senderId, String message){
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }
}
