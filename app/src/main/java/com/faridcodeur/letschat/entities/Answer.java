package com.faridcodeur.letschat.entities;

import java.io.Serializable;
import java.util.Date;

public class Answer implements Serializable {
    private int id;
    private int surveyId;
    private String userId;
    private String response;
    private Date created_at;

    public Answer() {
    }

    public Answer(int id, int surveyId, String userId, String response, Date created_at) {
        this.id = id;
        this.surveyId = surveyId;
        this.userId = userId;
        this.response = response;
        this.created_at = created_at;
    }

    public Answer(int surveyId, String userId, String response, Date created_at) {
        this.surveyId = surveyId;
        this.userId = userId;
        this.response = response;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String  getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
