package com.faridcodeur.letschat.survey.fragements;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSurveyViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public NewSurveyViewModel(Application application) {
        super(application);

    }

    public void createSurveys(Surveys surveys, @Nullable List<TextQuestion> textQuestionList,
                              @Nullable List<UniqueChoiceQuestion> uniqueChoiceQuestionList,
                              @Nullable List<MultipleChoiceQuestion> multipleChoiceQuestionList){

        List<Map<String, String>> questionsList = new ArrayList<>();

        assert textQuestionList != null;
        boolean hasError = false;
        for (TextQuestion textQuestion : textQuestionList){
            Map<String, String> map = textQuestion.get();
            if (map != null) {
                textQuestion.setSurveyId(surveys.getId());
            }else {
                hasError = true;
                break;
            }
        }

    }

}