package com.faridcodeur.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_survey_fragment);

        AtomicInteger ids = new AtomicInteger();

        LayoutInflater  layoutInflater = getLayoutInflater();

        LinearLayout layout = findViewById(R.id.survey_content_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ScrollView scrollView = findViewById(R.id.survey_content_layout_scroll);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.textQuestion) {
                TextQuestion textQuestions = new TextQuestion(this, this, layout);
                textQuestions.setId(ids.getAndIncrement());
                layout.addView(textQuestions.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.uniqueChoice){
                UniqueChoiceQuestion uniqueChoiceQuestion = new UniqueChoiceQuestion(this, this, layout);
                uniqueChoiceQuestion.setId(ids.getAndIncrement());
                layout.addView(uniqueChoiceQuestion.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }else if (item.getItemId() == R.id.multipleChoice){
                MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(this, this, layout);
                layout.addView(multipleChoiceQuestion.getView());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }

            return false;
        });

    }
}