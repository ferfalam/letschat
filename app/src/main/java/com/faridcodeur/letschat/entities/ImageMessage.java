package com.faridcodeur.letschat.entities;

import static android.os.Environment.getExternalStorageDirectory;

public class ImageMessage extends Message{

    public ImageMessage(String url, int messageType, String path) {
        super(url, messageType, path);
        this.message = url;
        this.messageType = messageType;
        this.path = path;
    }
}
