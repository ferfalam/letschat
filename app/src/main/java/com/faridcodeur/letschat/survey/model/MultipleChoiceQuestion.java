package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faridcodeur.letschat.R;
import com.google.android.material.textfield.TextInputEditText;

public class MultipleChoiceQuestion {
    private int id;
    private final View view;
    private final TextInputEditText question;
    private final TextInputEditText newRadioTextInput;

    @SuppressLint("InflateParams")
    public MultipleChoiceQuestion(AppCompatActivity appCompatActivity, Context context, LinearLayout linearLayout) {
        view = appCompatActivity.getLayoutInflater().inflate(R.layout.new_survey_multiple_choice_item, null);
        question = view.findViewById(R.id.question);
        newRadioTextInput = view.findViewById(R.id.new_checkbox_text_input);
        setListener(context, linearLayout);
    }

    public void setListener(Context context, LinearLayout linearLayout){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            Toast.makeText(context, "Vue " + id + " supprimé", Toast.LENGTH_SHORT).show();
            linearLayout.removeView(getView());
        });

        view.findViewById(R.id.addCheckbox).setOnClickListener(view1 -> {
            Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.deleteCheckbox).setOnClickListener(view1 -> {
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        });
    }

    public View getView() {
        return view;
    }

    public TextInputEditText getQuestion() {
        return question;
    }

    public TextInputEditText getNewRadioTextInput() {
        return newRadioTextInput;
    }

    public void setId(int id) {
        this.id = id;
    }
}
