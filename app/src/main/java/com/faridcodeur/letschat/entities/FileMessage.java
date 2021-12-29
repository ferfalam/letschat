package com.faridcodeur.letschat.entities;

public class FileMessage extends Message{

    public FileMessage(String url, int messageType, String path) {
        super(url, messageType, path);
        this.message = url;
        this.messageType = messageType;
        this.path = path;
    }
}
