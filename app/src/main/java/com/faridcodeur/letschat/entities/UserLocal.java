package com.faridcodeur.letschat.entities;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class UserLocal implements Serializable {
    private String id;
    private String phone_number;
    private String username;
    private Date last_connect;
    private Uri image_url;
    private Boolean online;
    private Boolean admin;

    public UserLocal(){

    }

    public UserLocal(String id, String phone_number, String username, Uri image_url, Boolean admin) {
        this.id = id;
        this.phone_number = phone_number;
        this.username = username;
        this.image_url = image_url;
        this.last_connect = new Date();
        this.online = true;
        this.admin = admin;
    }

    public UserLocal(String id, String phone_number, String username, Uri image_url) {
        this.id = id;
        this.phone_number = phone_number;
        this.username = username;
        this.image_url = image_url;
        this.last_connect = new Date();
        this.online = true;
        this.admin = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLast_connect() {
        return last_connect;
    }

    public void setLast_connect(Date lastConnect) {
        this.last_connect = lastConnect;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void addToDB(Map<String, Object> user){

    }


    public Uri getImage_url() {
        return image_url;
    }

    public void setImage_url(Uri image_url) {
        this.image_url = image_url;
    }
}
