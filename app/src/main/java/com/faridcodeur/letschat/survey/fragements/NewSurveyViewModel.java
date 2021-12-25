package com.faridcodeur.letschat.survey.fragements;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewSurveyViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public NewSurveyViewModel(Application application) {
        super(application);

    }

    public void createSurveys(Surveys surveys, @Nullable List<TextQuestion> textQuestionList,
                              @Nullable List<UniqueChoiceQuestion> uniqueChoiceQuestionList,
                              @Nullable List<MultipleChoiceQuestion> multipleChoiceQuestionList){

        assert textQuestionList != null;
        boolean hasError = false;
        for (TextQuestion textQuestion : textQuestionList){
            if (textQuestion.build()) {
                textQuestion.setSurveyId(surveys.getId());
            }else {
                hasError = true;
                break;
            }
        }
        assert uniqueChoiceQuestionList != null;
        for (UniqueChoiceQuestion uniqueChoiceQuestion : uniqueChoiceQuestionList){
            if (uniqueChoiceQuestion.build()) {
                uniqueChoiceQuestion.setSurveyId(surveys.getId());
            }else {
                hasError = true;
                break;
            }
        }

        assert multipleChoiceQuestionList != null;
        for (MultipleChoiceQuestion multipleChoiceQuestion : multipleChoiceQuestionList){
            if (multipleChoiceQuestion.build()) {
                multipleChoiceQuestion.setSurveyId(surveys.getId());
            }else break;
        }
    }

}