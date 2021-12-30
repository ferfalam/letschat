package com.faridcodeur.letschat.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.ActivitySondageBoxBinding;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SondageBoxActivity extends AppCompatActivity {
    ActivitySondageBoxBinding binding;
    Surveys survey;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySondageBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.sondageBoxToolbar);

        survey = (Surveys)getIntent().getSerializableExtra("survey");
        if (survey == null) {
            finish();
            return;
        }
        binding.theme.setText(survey.getTitle());
        binding.delay.setText(DateUtils.getRelativeTimeSpanString(survey.getCreated_at().getTime(), new Date().getTime(), 0));

        //TODO replace if condition by condition :: user.getId() == survey.getUserId()
        if (!Objects.equals(FirebaseAuth.getInstance().getUid(), String.valueOf(survey.getUserId()))){
            if (survey.isDisabled()) {
                binding.soumetre.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
                binding.soumetre.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF8B83")));
            }else {
                binding.soumetre.setImageDrawable(getResources().getDrawable(R.drawable.ic_validate));
                binding.soumetre.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#86ED8B")));
            }
        }
        //TODO Fin

        buildView();

        binding.soumetre.setOnClickListener(view -> {
            Toast.makeText(SondageBoxActivity.this, "Validate" , Toast.LENGTH_SHORT).show();
        });

    }

    @SuppressLint("SetTextI18n")
    void buildView() {
        List<Map<String, String>> questionsList = new ArrayList<>();

        questionsList = new Gson().fromJson(survey.getQuestions(), questionsList.getClass());
        questionsList = InputValidation.sortMapById(questionsList);


        int i = 0;
        for (Map<String, String> question : questionsList){
            switch (Objects.requireNonNull(question.get("type"))) {
                case "text": {
                    @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_text_type, null);

                    TextView textView = myView.findViewById(R.id.question);
                    textView.setText(++i + ". " + question.get("question"));
                    binding.surveyContentLayout.addView(myView);
                    break;
                }
                case "radio": {
                    @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_unichoice_type, null);
                    TextView textView = myView.findViewById(R.id.question);
                    textView.setText(++i + ". " + question.get("question"));

                    RadioGroup radioGroup = myView.findViewById(R.id.radioOption);

                    for (String radioText : new Gson().fromJson(question.get("items"), String[].class)) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(radioText);
                        radioGroup.addView(radioButton);
                    }
                    binding.surveyContentLayout.addView(myView);
                    break;
                }
                case "checkbox": {
                    @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_multichoice_type, null);

                    TextView textView = myView.findViewById(R.id.questionmultiple);
                    textView.setText(++i + ". " + question.get("question"));

                    LinearLayout linearLayout = myView.findViewById(R.id.enter);

                    for (String checkboxText : new Gson().fromJson(question.get("items"), String[].class)) {
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setText(checkboxText);
                        linearLayout.addView(checkBox);
                    }
                    binding.surveyContentLayout.addView(myView);
                    break;
                }
            }
        }
    }
}

class AnswerModel {
    private int questionId;
}