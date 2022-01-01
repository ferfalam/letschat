package com.faridcodeur.letschat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.ActivitySondageBoxBinding;
import com.faridcodeur.letschat.entities.Answer;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.fragments.SurveysFragment;
import com.faridcodeur.letschat.utiles.Global;
import com.faridcodeur.letschat.utiles.InputValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SondageBoxActivity extends AppCompatActivity {
    ActivitySondageBoxBinding binding;
    Surveys survey;
    List<AnswerModel> answerModelList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySondageBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        survey = (Surveys)getIntent().getSerializableExtra("survey");
        if (survey == null) {
            finish();
            return;
        }
        binding.theme.setText(survey.getTitle());
        binding.surveyState.setText(DateUtils.getRelativeTimeSpanString(survey.getCreated_at().getTime(), new Date().getTime(), 0));

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(String.valueOf(survey.getUserId()))){
            binding.soumetre.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
            binding.soumetre.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF8B83")));
            binding.answer.setVisibility(View.VISIBLE);
        }

        buildView();

        binding.answer.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(String.valueOf(survey.getUserId()))) {
                Intent intent = new Intent(SondageBoxActivity.this, AnswerSondageActivity.class);
                intent.putExtra("survey", survey);
                startActivity(intent);
            }
        });

        binding.soumetre.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(String.valueOf(survey.getUserId()))){
                Snackbar.make(binding.getRoot(), "Voulez-vous vraiment suppimer ce sondage ?", Snackbar.LENGTH_LONG)
                        .setAction("Oui", view1 -> {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection(Global.getSurveysCollectionPath()).document(survey.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> Toast.makeText(SondageBoxActivity.this, "Le sondage à bien été supprimé" , Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(SondageBoxActivity.this, "Une erreur est survenue lors de la suppression" , Toast.LENGTH_SHORT).show());
                            SurveysFragment.surveys.removeIf(survey1 -> survey1.equals(survey));
                            finish();
                        }).show();
            }else {
                if (submitResult()){
                    Toast.makeText(SondageBoxActivity.this, "Votre reponse à été envoyer. Merci pour la participation" , Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(SondageBoxActivity.this, "Erreur de soumission. Veuillez verifier si tous les questions sont repondu" , Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.sondageView.setOnClickListener(v -> onBackPressed());


    }

    @SuppressLint("SetTextI18n")
    void buildView() {
        List<Map<String, String>> questionsList = new ArrayList<>();
        questionsList = new Gson().fromJson(survey.getQuestions(), questionsList.getClass());
        questionsList = InputValidation.sortMapById(questionsList, "id");


        int i = 0;
        for (Map<String, String> question : questionsList){
            switch (Objects.requireNonNull(question.get("type"))) {
                case "text": {
                    @SuppressLint("InflateParams") LinearLayout myView = (LinearLayout) getLayoutInflater().inflate(R.layout.question_text_type, null);

                    TextView textView = myView.findViewById(R.id.question);
                    textView.setText(++i + ". " + question.get("question"));
                    answerModelList.add(new AnswerModel(Integer.parseInt(Objects.requireNonNull(question.get("id"))), "text", myView, this));
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
                    answerModelList.add(new AnswerModel(Integer.parseInt(Objects.requireNonNull(question.get("id"))), "radio", myView, this));
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
                    answerModelList.add(new AnswerModel(Integer.parseInt(Objects.requireNonNull(question.get("id"))), "checkbox", myView, this));

                    binding.surveyContentLayout.addView(myView);
                    break;
                }
            }
        }
    }

    public boolean submitResult(){
        List<Map<String, String>> responseList = new ArrayList<>();
        for (AnswerModel answerModel : answerModelList){
            Map<String , String> map = answerModel.get();
            if (map != null){
                responseList.add(map);
            }else return false;
        }
        if (responseList.size() != 0) {
            Answer answer = new Answer(survey.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), new Gson().toJson(responseList), new Date());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Global.getAnswerCollectionPath())
                    .add(answer)
                    .addOnSuccessListener(documentReference -> Log.d("submitResult", "Nouveau answer crée avec l'id: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.d("submitResult", "Erreur lors de l'ajout du document: " + e));
            return true;
        }
        return false;
    }
}

class AnswerModel {
    private final int questionId;
    private final String type;
    private final View view;
    private final Context context;

    public AnswerModel(int questionId, String type, View view, Context context) {
        this.questionId = questionId;
        this.type = type;
        this.view = view;
        this.context = context;
    }

    public Map<String, String> get(){
        Map<String, String> map = new HashMap<>();
        switch (this.type) {
            case "text":
                TextInputEditText champ = view.findViewById(R.id.champ);
                if (!InputValidation.isEmptyInput(champ, false)) {
                    map.put("questionId", String.valueOf(questionId));
                    map.put("type", type);
                    map.put("value", Objects.requireNonNull(champ.getText()).toString());
                    return map;
                } else {
                    champ.setError("Aucune reponse renseigner");
                }
                break;
            case "radio":
                RadioGroup radioGroup = view.findViewById(R.id.radioOption);
                if (radioGroup.getCheckedRadioButtonId() != -1){
                    map.put("questionId", String.valueOf(questionId));
                    map.put("type", type);
                    map.put("value", ((RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
                    return map;
                }else {
                    Toast.makeText(context, "Veuillez remplir tous les infos" , Toast.LENGTH_SHORT).show();
                }
                break;
            case "checkbox":
                LinearLayout linearLayout = view.findViewById(R.id.enter);
                List<String> selectedCheckboxText = new ArrayList<>();
                for (int i=0; i < linearLayout.getChildCount(); i++){
                    CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                    if (checkBox.isChecked()){
                        selectedCheckboxText.add(checkBox.getText().toString());
                    }
                }

                if (selectedCheckboxText.size() != 0){
                    map.put("questionId", String.valueOf(questionId));
                    map.put("type", type);
                    map.put("values", new Gson().toJson(selectedCheckboxText));
                    return map;
                }
                break;
        }
        return null;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getType() {
        return type;
    }

    public View getView() {
        return view;
    }
}