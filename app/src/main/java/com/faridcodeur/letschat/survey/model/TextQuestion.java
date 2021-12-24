package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TextQuestion implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "surveyId")
    private int surveyId;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "response")
    private String response;

    @Ignore
    private View view;

    @SuppressLint("InflateParams")
    @Ignore
    public TextQuestion(Fragment fragment, LinearLayout linearLayout, List<TextQuestion> textQuestionList) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_textquestion_item, null);
        setListener(linearLayout, textQuestionList);
    }

    public TextQuestion(int id, String question, String response) {
        this.id = id;
        this.question = question;
        this.response = response;
    }

    public void setListener(LinearLayout linearLayout, List<TextQuestion> textQuestionList){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
            textQuestionList.remove(this);
        });
    }

    @SuppressLint("CutPasteId")
    public boolean build(){
        TextInputEditText question = view.findViewById(R.id.question);
        if (!InputValidation.isEmptyInput(question, false)){
            this.question = Objects.requireNonNull(question.getText()).toString();
            return true;
        }else {question.setError("Aucune question renseigner");}
        return false;
    }

    public View getView() {
        return view;
    }

    public String getQuestion() {
        return Objects.requireNonNull(((TextInputEditText)view.findViewById(R.id.question)).getText()).toString();
    }

    public String getResponse() {
        return Objects.requireNonNull(((TextInputEditText)view.findViewById(R.id.response)).getText()).toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
}
