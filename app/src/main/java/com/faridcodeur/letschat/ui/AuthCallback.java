package com.faridcodeur.letschat.ui;

public interface    AuthCallback {

    void sendMessage(String phoneNumber);
    void verification(String code);

}
