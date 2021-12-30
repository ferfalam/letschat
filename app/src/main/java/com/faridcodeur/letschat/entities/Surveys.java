package com.faridcodeur.letschat.entities;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class Surveys implements Serializable {
    private int id;
    private String title;
    private String description;
    private String questions;
    private Timestamp created_at;
    public static String collectionPath = "surveys";

    public Surveys() {
        this.created_at = new Timestamp(new Date());
    }

    public Surveys(String title, String description, String questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.created_at = new Timestamp(new Date());
    }

    public Surveys(int id, String title, String description, String questions, Timestamp created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.created_at = created_at;
    }

    public Surveys(String title, String description) {
        this.title = title;
        this.description = description;
        this.created_at = new Timestamp(new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
