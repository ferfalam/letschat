package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faridcodeur.letschat.R;
import com.google.android.material.textfield.TextInputEditText;

public class TextQuestion {
    private int id;
    private final View view;
    private final TextInputEditText question;
    private final TextInputEditText response;

    @SuppressLint("InflateParams")
    public TextQuestion(AppCompatActivity appCompatActivity, Context context, LinearLayout linearLayout) {
        view = appCompatActivity.getLayoutInflater().inflate(R.layout.new_survey_textquestion_item, null);
        question = view.findViewById(R.id.question);
        response = view.findViewById(R.id.response);
        setListener(context, linearLayout);
    }

    public void setListener(Context context, LinearLayout linearLayout){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            Toast.makeText(context, "Vue " + id + " supprimé", Toast.LENGTH_SHORT).show();
            linearLayout.removeView(getView());
        });
    }

    public View getView() {
        return view;
    }

    public TextInputEditText getQuestion() {
        return question;
    }

    public TextInputEditText getResponse() {
        return response;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
