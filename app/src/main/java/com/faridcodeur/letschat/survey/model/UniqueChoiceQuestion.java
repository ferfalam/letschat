package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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

public class UniqueChoiceQuestion implements Serializable {
    private int id;
    private int surveyId;
    private String question;
    private String radioTextList;

    private View view;
    private LinearLayout radioLayout;
    protected final List<RadioButton> radioButtonList = new ArrayList<>();

    @SuppressLint("InflateParams")
    public UniqueChoiceQuestion(Fragment fragment, LinearLayout linearLayout, List<UniqueChoiceQuestion> uniqueChoiceQuestionList) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_unique_choice_item, null);
        radioLayout = view.findViewById(R.id.radioLayout);
        setListener(fragment, linearLayout, uniqueChoiceQuestionList);
    }

    public UniqueChoiceQuestion(int id, int surveyId, String question, String radioTextList) {
        this.id = id;
        this.surveyId = surveyId;
        this.question = question;
        this.radioTextList = radioTextList;
    }

    public void setListener(Fragment fragment, LinearLayout linearLayout, List<UniqueChoiceQuestion> uniqueChoiceQuestionList){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
            uniqueChoiceQuestionList.remove(this);
        });

        view.findViewById(R.id.addRadio).setOnClickListener(view1 -> {
            if (!InputValidation.isEmptyInput(view.findViewById(R.id.new_radio_text_input), false)){
                RadioButtonLayout radioButtonLayout = new RadioButtonLayout(fragment, radioLayout, Objects.requireNonNull(((TextInputEditText) view.findViewById(R.id.new_radio_text_input)).getText()).toString(), radioButtonList);
                radioLayout.addView(radioButtonLayout.getView());
                radioButtonList.add(radioButtonLayout.getRadioButton());
                ((TextInputEditText) view.findViewById(R.id.new_radio_text_input)).setText(null);
            }
        });
    }

    public boolean build(){
        TextInputEditText question = view.findViewById(R.id.question);
        if (!InputValidation.isEmptyInput(question, false)){
            this.question = Objects.requireNonNull(question.getText()).toString();
            List<String> list = new ArrayList<>();
            for (RadioButton radioButton: radioButtonList) {
                list.add(radioButton.getText().toString());
            }
            radioTextList = new Gson().toJson(list);
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRadioTextList() {
        return radioTextList;
    }

    public void setRadioTextList(String radioTextList) {
        this.radioTextList = radioTextList;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
}

class RadioButtonLayout{
    private final View view;
    private final RadioButton radioButton;

    @SuppressLint("InflateParams")
    public RadioButtonLayout(Fragment fragment, LinearLayout parent, String radioText, List<RadioButton> radioButtonList) {
        view = fragment.getLayoutInflater().inflate(R.layout.radio_button_layout_content, null);
        radioButton = view.findViewById(R.id.radio);
        radioButton.setText(radioText);
        view.findViewById(R.id.deleteRadio).setOnClickListener(view1 -> {
            parent.removeView(getView());
            radioButtonList.remove(getRadioButton());
        });
    }

    public View getView() {
        return view;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }
}
