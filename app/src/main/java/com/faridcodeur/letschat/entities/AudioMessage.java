package com.faridcodeur.letschat.entities;

public class AudioMessage extends Message{

    public AudioMessage(String url, int messageType, String path) {
        super(url, messageType, path);
        this.message = url;
        this.messageType = messageType;
        this.path = path;
    }
}
