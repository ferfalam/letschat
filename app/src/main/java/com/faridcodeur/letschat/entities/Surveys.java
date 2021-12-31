package com.faridcodeur.letschat.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Surveys implements Serializable {
    private String id;
    private String title;
    private String description;
    private String questions;
    private Date created_at;
    private String userId;

    public Surveys() {
    }

    public Surveys(String id, String title, String description, String questions, Date created_at, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.created_at = created_at;
        this.userId = userId;
    }

    public Surveys(String title, String description, String  userId) {
        this.title = title;
        this.description = description;
        this.created_at = new Date();
        this.userId = userId;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String  getUserId() {
        return userId;
    }

    public void setUserId(String  userId) {
        this.userId = userId;
    }



}
