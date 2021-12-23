package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
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

public class MultipleChoiceQuestion {
    private int id;
    private View view;
    private LinearLayout checkboxLayout;
    private TextInputEditText question;
    protected final List<CheckBox> checkBoxList = new ArrayList<>();

    @SuppressLint("InflateParams")
    public MultipleChoiceQuestion(Fragment fragment, LinearLayout linearLayout) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_multiple_choice_item, null);
        checkboxLayout = view.findViewById(R.id.checkboxLayout);
        question = view.findViewById(R.id.question);
        setListener(fragment, linearLayout);
    }

    public MultipleChoiceQuestion() {

    }

    public void setListener(Fragment fragment, LinearLayout linearLayout){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
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

    public View getView() {
        return view;
    }

    public String getQuestion() {
        return Objects.requireNonNull(question.getText()).toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CheckBox> getCheckBoxList() {
        return checkBoxList;
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
