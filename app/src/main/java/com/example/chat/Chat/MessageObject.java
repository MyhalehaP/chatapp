package com.example.chat.Chat;

import java.util.ArrayList;

public class MessageObject {
    private String messageId,senderId,message, senderName;
    ArrayList<String> mediaUrlList;
    public MessageObject(String messageId,String senderId, String senderName, String message, ArrayList<String> mediaUrlList){
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.mediaUrlList = mediaUrlList;
    }

    public String getSenderName() {
        return senderName;
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

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }
}
