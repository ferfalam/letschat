package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class TextQuestion {
    private View view;
    //Database params
    private int id;
    private String question;
    private String response;

    @SuppressLint("InflateParams")
    public TextQuestion(Fragment fragment, LinearLayout linearLayout) {
        view = fragment.getLayoutInflater().inflate(R.layout.new_survey_textquestion_item, null);
        setListener(fragment, linearLayout);
    }

    public TextQuestion(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public void setListener(Fragment fragment, LinearLayout linearLayout){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
        });
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
}
