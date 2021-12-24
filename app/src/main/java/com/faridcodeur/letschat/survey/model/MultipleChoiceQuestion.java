package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
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
public class MultipleChoiceQuestion implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "surveyId")
    private int surveyId;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "checkBoxTextList")
    private String checkBoxTextList;

    @Ignore
    private View view;
    @Ignore
    private LinearLayout checkboxLayout;

    @Ignore
    protected final List<CheckBox> checkBoxList = new ArrayList<>();

    @SuppressLint("InflateParams")
    @Ignore
    public MultipleChoiceQuestion(Fragment fragment, LinearLayout linearLayout, List<MultipleChoiceQuestion> multipleChoiceQuestionList) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_multiple_choice_item, null);
        checkboxLayout = view.findViewById(R.id.checkboxLayout);
        setListener(fragment, linearLayout, multipleChoiceQuestionList);
    }

    public MultipleChoiceQuestion(int id, int surveyId, String question, String checkBoxTextList) {
        this.id = id;
        this.surveyId = surveyId;
        this.question = question;
        this.checkBoxTextList = checkBoxTextList;
    }

    public void setListener(Fragment fragment, LinearLayout linearLayout, List<MultipleChoiceQuestion> multipleChoiceQuestionList){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
            multipleChoiceQuestionList.remove(this);
        });

        view.findViewById(R.id.addCheckbox).setOnClickListener(view1 -> {
            if (!InputValidation.isEmptyInput(view.findViewById(R.id.new_checkbox_text_input), false)){
                CheckButtonLayout checkButtonLayout = new CheckButtonLayout(fragment, checkboxLayout, Objects.requireNonNull(((TextInputEditText) view.findViewById(R.id.new_checkbox_text_input)).getText()).toString(), checkBoxList);
                checkboxLayout.addView(checkButtonLayout.getView());
                checkBoxList.add(checkButtonLayout.getCheckBox());
                ((TextInputEditText) view.findViewById(R.id.new_checkbox_text_input)).setText(null);
            }
        });

    }

    public boolean build(){
        TextInputEditText question = view.findViewById(R.id.question);
        if (!InputValidation.isEmptyInput(question, false)){
            this.question = Objects.requireNonNull(question.getText()).toString();
            List<String> list = new ArrayList<>();
            for (CheckBox checkBox: checkBoxList) {
                list.add(checkBox.getText().toString());
            }
            checkBoxTextList = new Gson().toJson(list);
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCheckBoxTextList() {
        return checkBoxTextList;
    }

    public void setCheckBoxTextList(String checkBoxTextList) {
        this.checkBoxTextList = checkBoxTextList;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
}

class CheckButtonLayout{
    private int id;
    private final View view;
    private final CheckBox checkBox;

    @SuppressLint("InflateParams")
    public CheckButtonLayout(Fragment fragment, LinearLayout parent, String checkboxText, List<CheckBox> checkBoxList) {
        view = fragment.getLayoutInflater().inflate(R.layout.check_button_layout_content, null);
        checkBox = view.findViewById(R.id.checkbox);
        checkBox.setText(checkboxText);
        view.findViewById(R.id.deleteCheckbox).setOnClickListener(view1 -> {
            parent.removeView(getView());
            checkBoxList.remove(getCheckBox());
        });
    }

    public View getView() {
        return view;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
