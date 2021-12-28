package com.faridcodeur.letschat.survey.model;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TextQuestion implements Serializable {
    private int id;
    private String question;
    private View view;

    @SuppressLint("InflateParams")
    public TextQuestion(int id, Fragment fragment, LinearLayout linearLayout, List<TextQuestion> textQuestionList) {
        this.view = fragment.getLayoutInflater().inflate(R.layout.new_survey_textquestion_item, null);
        this.id = id;
        setListener(linearLayout, textQuestionList);
    }

    public void setListener(LinearLayout linearLayout, List<TextQuestion> textQuestionList){
        view.findViewById(R.id.delView).setOnClickListener(view1 -> {
            linearLayout.removeView(getView());
            textQuestionList.remove(this);
        });
    }

    @SuppressLint("CutPasteId")
    public Map<String, String> get(){
        TextInputEditText question = view.findViewById(R.id.question);
        if (!InputValidation.isEmptyInput(question, false)){
            this.question = Objects.requireNonNull(question.getText()).toString();
            Map<String, String> textQuestion = new HashMap<>();
            textQuestion.put("id", Integer.toString(this.id));
            textQuestion.put("type", "text");
            textQuestion.put("response", null);
            textQuestion.put("question", this.question);
            return textQuestion;
        }else {question.setError("Aucune question renseigner");}
        return null;
    }

    public View getView() {
        return view;
    }

    public String getQuestion() {
        return Objects.requireNonNull(((TextInputEditText)view.findViewById(R.id.question)).getText()).toString();
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
}
