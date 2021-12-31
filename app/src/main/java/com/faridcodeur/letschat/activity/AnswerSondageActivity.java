package com.faridcodeur.letschat.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.ActivityAnswerSondageBinding;
import com.faridcodeur.letschat.entities.Answer;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.utiles.Global;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnswerSondageActivity extends AppCompatActivity {

    ActivityAnswerSondageBinding binding;
    List<Answer> answers = new ArrayList<>();
    Surveys survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnswerSondageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        survey = (Surveys)getIntent().getSerializableExtra("survey");
        if (survey == null) {
            finish();
            return;
        }

        binding.theme.setText(survey.getTitle());
        binding.surveyState.setText(DateUtils.getRelativeTimeSpanString(survey.getCreated_at().getTime(), new Date().getTime(), 0));
        binding.sondageView.setOnClickListener(v -> onBackPressed());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Global.getAnswerCollectionPath())
                .whereEqualTo("surveyId", survey.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Answer answer = documentSnapshot.toObject(Answer.class);
                            answer.setId(documentSnapshot.getId());
                            answers.add(answer);
                        }
                        buildView();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public void buildView() {

        List<Map<String, String>> questionsList = new ArrayList<>();
        questionsList = new Gson().fromJson(survey.getQuestions(), questionsList.getClass());
        questionsList = InputValidation.sortMapById(questionsList, "id");

        if (answers.size() == 0) {
            binding.textView.setText("Aucune reponse pour ce sondage");
            return;
        }

        for (Answer answer : answers){
            List<Map<String, String>> responseList = new ArrayList<>();
            responseList = new Gson().fromJson(answer.getResponse(), responseList.getClass());

            int i = 0;
            @SuppressLint("InflateParams") MaterialCardView resultCard = (MaterialCardView) getLayoutInflater().inflate(R.layout.result_box, null);
            @SuppressLint("InflateParams") LinearLayout resultBox = resultCard.findViewById(R.id.result_content_layout);
            ((TextView)resultCard.findViewById(R.id.created_at)).setText(DateUtils.getRelativeTimeSpanString(answer.getCreated_at().getTime(), new Date().getTime(), 0));
            resultCard.clearChildFocus(resultBox);
            resultCard.setOnLongClickListener(view -> {
                resultCard.setChecked(!resultCard.isChecked());
                return true;
            });

            for (Map<String, String> question : questionsList){
                switch (Objects.requireNonNull(question.get("type"))) {
                    case "text": {
                        @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.result_view, null);

                        ((TextView)myView.findViewById(R.id.question)).setText(++i + ". " + question.get("question"));

                        for (Map<String, String> map : responseList){
                            if (map.get("questionId").equals(question.get("id")) && map.get("type").equals("text")){
                                ((TextView)myView.findViewById(R.id.result)).setText(map.get("value"));
                            }
                        }

                        resultBox.addView(myView);
                        break;
                    }
                    case "radio": {
                        @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_unichoice_type, null);
                        TextView textView = myView.findViewById(R.id.question);
                        textView.setText(++i + ". " + question.get("question"));

                        RadioGroup radioGroup = myView.findViewById(R.id.radioOption);

                        for (String radioText : new Gson().fromJson(question.get("items"), String[].class)) {
                            RadioButton radioButton = new RadioButton(this);

                            for (Map<String, String> map : responseList){
                                if (map.get("questionId").equals(question.get("id")) && map.get("type").equals("radio") && radioText.equals(map.get("value"))){
                                    radioButton.setChecked(true);
                                    radioButton.clearAnimation();
                                    radioButton.setClickable(false);
                                    radioButton.setText(radioText);
                                    radioGroup.addView(radioButton);
                                }
                            }
                        }
                        resultBox.addView(myView);
                        break;
                    }
                    case "checkbox": {
                        @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_multichoice_type, null);

                        TextView textView = myView.findViewById(R.id.questionmultiple);
                        textView.setText(++i + ". " + question.get("question"));

                        LinearLayout linearLayout = myView.findViewById(R.id.enter);

                        for (String checkboxText : new Gson().fromJson(question.get("items"), String[].class)) {
                            CheckBox checkBox = new CheckBox(this);

                            for (Map<String, String> map : responseList){
                                if (map.get("questionId").equals(question.get("id")) && map.get("type").equals("checkbox")){
                                    String[] strings = new String[0];
                                    strings = new Gson().fromJson(map.get("values"), strings.getClass());
                                    for (String str : strings){
                                        if (str.equals(checkboxText)){
                                            checkBox.setChecked(true);
                                            checkBox.setClickable(false);
                                            checkBox.clearAnimation();
                                            checkBox.setText(checkboxText);
                                            linearLayout.addView(checkBox);
                                        }
                                    }
                                }
                            }
                        }

                        resultBox.addView(myView);
                        break;
                    }
                }
            }
            binding.answerList.addView(resultCard);

        }

    }
}