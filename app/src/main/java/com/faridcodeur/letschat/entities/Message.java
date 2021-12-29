package com.faridcodeur.letschat.entities;

import java.util.Date;

public class Message {

        public String message;
        public int messageType;
        public String path;
        public Date messageTime = new Date();
        // Constructor
        public Message(String message, int messageType, String path) {
            this.message = message;
            this.messageType = messageType;
            this.path = path;
        }
}
