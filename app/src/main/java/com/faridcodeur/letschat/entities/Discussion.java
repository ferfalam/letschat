package com.faridcodeur.letschat.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Discussion implements Serializable {

    private String senderId;
    private String targetName;
    private String senderName;
    private Message lastMessage;
    private String receiverID;
    private String receiverName;
    private String profileImg;
    private Date lastTime;
    private ArrayList<Message> messages;
    public static String collectionPath = "discussions";
    private ArrayList<String> ff = new ArrayList<>();

    public ArrayList<String> getFf() {
        return ff;
    }

    public void setFf(ArrayList<String> ff) {
        this.ff = ff;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

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

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Discussion(){

    }
    public Discussion(String senderName,String senderId, String targetName, Message lastMessage, String receiver, String profileImg, Date lastTime, ArrayList<Message> messageArrayList) {
        this.senderId = senderId;
        this.targetName = targetName;
        this.lastMessage = lastMessage;
        this.receiverID = receiver;
        this.profileImg = profileImg;
        this.lastTime = lastTime;
        this.messages = messageArrayList;
        this.senderName = senderName;
        this.ff.add(0, receiver);
        this.ff.add(1, senderId);
    }
}
