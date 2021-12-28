package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MultipleChoiceQuestion implements Serializable {
    private int id;
    private String question;
    private View view;
    private LinearLayout checkboxLayout;

    protected final List<CheckBox> checkBoxList = new ArrayList<>();

    @SuppressLint("InflateParams")
    public MultipleChoiceQuestion(int id, Fragment fragment, LinearLayout linearLayout, List<MultipleChoiceQuestion> multipleChoiceQuestionList) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_multiple_choice_item, null);
        checkboxLayout = view.findViewById(R.id.checkboxLayout);
        this.id = id;
        setListener(fragment, linearLayout, multipleChoiceQuestionList);
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

    public Map<String, String> get(){
        TextInputEditText question = view.findViewById(R.id.question);
        if (!InputValidation.isEmptyInput(question, false)){

            this.question = Objects.requireNonNull(question.getText()).toString();
            List<String> list = new ArrayList<>();
            for (CheckBox checkBox: checkBoxList) {
                list.add(checkBox.getText().toString());
            }

            Map<String, String> multipleQuestion = new HashMap<>();
            multipleQuestion.put("id", Integer.toString(this.id));
            multipleQuestion.put("type", "checkbox");
            multipleQuestion.put("question", this.question);
            multipleQuestion.put("items", new Gson().toJson(list));

            return multipleQuestion;
        }else {question.setError("Aucune question renseigner");}
        return null;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

class CheckButtonLayout{
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
