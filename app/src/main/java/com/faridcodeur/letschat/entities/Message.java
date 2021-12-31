package com.faridcodeur.letschat.entities;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    public String message;
    public String senderID;
    public int messageType;
    public String path;
    public Date messageTime;
    public static String collectionPath = "messages";
    public String downloadUri;

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessageText() {
        return message;
    }

    public void setMessageText(String messageText) {
        this.message = messageText;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessagePath() {
        return path;
    }

    public void setMessagePath(String path) {
        this.path = path;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    // Constructor
    public Message(String sender, String message, int messageType, String path) {
        this.senderID =sender;
        this.message = message;
        this.messageType = messageType;
        this.path = path;
        this.messageTime = new Date();
    }

    public  Message(){
        this.messageTime = messageTime;
    }
}
