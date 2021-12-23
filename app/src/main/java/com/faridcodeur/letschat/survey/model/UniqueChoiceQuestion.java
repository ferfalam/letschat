package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UniqueChoiceQuestion {
    private View view;
    private LinearLayout radioLayout;
    protected final List<RadioButton> radioButtonList = new ArrayList<>();

    //Database params
    private int id;
    private String question;

    @SuppressLint("InflateParams")
    public UniqueChoiceQuestion(Fragment fragment, LinearLayout linearLayout) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_unique_choice_item, null);
        radioLayout = view.findViewById(R.id.radioLayout);
        setListener(fragment, linearLayout);
    }

    public UniqueChoiceQuestion(String question) {
        this.question = question;
    }

    public void setListener(Fragment fragment, LinearLayout linearLayout){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
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

    public List<RadioButton> getRadioButtonList() {
        return radioButtonList;
    }
}

class RadioButtonLayout{
    private View view;
    private RadioButton radioButton;

    //Database params
    private int id;
    private int uniqueChoiceQuestionId;
    private String radioText;

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

    public RadioButtonLayout(int uniqueChoiceQuestionId, String radioText) {
        this.uniqueChoiceQuestionId = uniqueChoiceQuestionId;
        this.radioText = radioText;
    }

    public View getView() {
        return view;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public int getUniqueChoiceQuestionId() {
        return uniqueChoiceQuestionId;
    }

    public void setUniqueChoiceQuestionId(int uniqueChoiceQuestionId) {
        this.uniqueChoiceQuestionId = uniqueChoiceQuestionId;
    }

    public String getRadioText() {
        return radioText;
    }

    public void setRadioText(String radioText) {
        this.radioText = radioText;
    }
}
