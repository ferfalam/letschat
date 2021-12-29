package com.faridcodeur.letschat.entities;

import java.io.Serializable;

public class Surveys implements Serializable {
    private int id;
    private String title;
    private String description;
    private String questions;
    private String created_at;

    public Surveys(String title, String description, String questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public Surveys(int id, String title, String description, String questions, String created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_at = questions;
        this.created_at = created_at;
    }

    public Surveys(String title, String description) {
        this.title = title;
        this.description = description;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
