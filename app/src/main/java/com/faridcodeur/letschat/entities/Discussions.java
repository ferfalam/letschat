package com.faridcodeur.letschat.entities;

import java.io.Serializable;

public class Discussions implements Serializable {

    private String name;
    private String profileImg;
    private String message;
    private String time;


    public Discussions(String name, String profileImg, String messsage, String time) {
        this.name = name;
        this.profileImg = profileImg;
        this.message=messsage;
        this.time=time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
