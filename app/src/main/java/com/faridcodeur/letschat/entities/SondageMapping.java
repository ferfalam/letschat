package com.faridcodeur.letschat.entities;


import java.util.ArrayList;
import java.util.List;

public class SondageMapping {


    private int id;
    private String question;
    private String questionType;
    private int numberOfIndexQuestionType;
    private List<String> listString;


    public SondageMapping() {
    }

    public SondageMapping(String question, String questionType, int numberOfIndexQuestionType , List<String> listString) {
        this.question = question;
        this.questionType = questionType;
        this.numberOfIndexQuestionType = numberOfIndexQuestionType;
        this.listString = listString;
    }

    public SondageMapping(String question, String questionType) {
        this.question = question;
        this.questionType = questionType;
    }

    public int getId() {
        return id;
    }

    public List<String> getListString() {
        return listString;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public int getNumberOfIndexQuestionType() {
        return numberOfIndexQuestionType;
    }

    @Override
    public String toString() {
        return "SondageMapping{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", questionType='" + questionType + '\'' +
                ", numberOfIndexQuestionType=" + numberOfIndexQuestionType +
                '}';
    }
}
