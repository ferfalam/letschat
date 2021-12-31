package com.faridcodeur.letschat.entities;

import static android.os.Environment.getExternalStorageDirectory;

import android.net.Uri;

public class ImageMessage extends Message{

    public ImageMessage(String userId,String url, int messageType, String path) {
        super(userId,url, messageType, path);
        this.message = url;
        this.messageType = messageType;
        this.path = path;
    }
}
