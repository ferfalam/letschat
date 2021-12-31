package com.faridcodeur.letschat.entities;

import android.net.Uri;

public class FileMessage extends Message{

    public FileMessage(String userId, String url, int messageType, String path) {
        super(userId, url, messageType, path);
        this.message = url;
        this.messageType = messageType;
        this.path = path;
    }
}
