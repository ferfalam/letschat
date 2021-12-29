package com.faridcodeur.letschat.survey.fragements;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSurveyViewModel extends AndroidViewModel {

    public NewSurveyViewModel(Application application) {
        super(application);
    }

    public boolean createSurveys(Surveys surveys, @Nullable List<TextQuestion> textQuestionList,
                              @Nullable List<UniqueChoiceQuestion> uniqueChoiceQuestionList,
                              @Nullable List<MultipleChoiceQuestion> multipleChoiceQuestionList){

        List<Map<String, String>> questionsList = new ArrayList<>();

        if (textQuestionList != null && textQuestionList.size() != 0) {
            for (TextQuestion textQuestion : textQuestionList) {
                Map<String, String> map = textQuestion.get();
                if (map != null) {
                    questionsList.add(map);
                } else return false;
            }
        }

        if (uniqueChoiceQuestionList != null && uniqueChoiceQuestionList.size() != 0) {
            for (UniqueChoiceQuestion uniqueChoiceQuestion : uniqueChoiceQuestionList) {
                Map<String, String> map = uniqueChoiceQuestion.get();
                if (map != null) {
                    questionsList.add(map);
                } else return false;
            }
        }

        if (multipleChoiceQuestionList != null && multipleChoiceQuestionList.size() != 0) {
            for (MultipleChoiceQuestion multipleChoiceQuestion : multipleChoiceQuestionList){
                Map<String , String> map = multipleChoiceQuestion.get();
                if (map != null){
                    questionsList.add(map);
                }else return false;
            }
        }

        surveys.setQuestions(new Gson().toJson(questionsList));
        Log.i("TEST", "Test: " + surveys.getQuestions());

        return true;
    }
}