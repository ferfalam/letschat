package com.faridcodeur.letschat.entities;

import java.io.Serializable;

public class Discussion implements Serializable {

    private String senderId;
    private String targetName;
    private Message lastMessage;
    private String receiverID;
    private String profileImg;
    private String lastTime;
    public static String collectionPath = "discussions";

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Discussion(String senderId, String targetName, Message lastMessage, String receiver, String profileImg, String lastTime) {
        this.senderId = senderId;
        this.targetName = targetName;
        this.lastMessage = lastMessage;
        this.receiverID = receiver;
        this.profileImg = profileImg;
        this.lastTime = lastTime;
    }
}
