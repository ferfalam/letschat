package com.faridcodeur.letschat.survey.fragements;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.faridcodeur.letschat.dao.DataBaseRoom;
import com.faridcodeur.letschat.dao.QuestionRoomDao;
import com.faridcodeur.letschat.dao.SurveyRoomDao;
import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewSurveyViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private final SurveyRoomDao surveyRoomDao;
    private final QuestionRoomDao questionRoomDao;

    public NewSurveyViewModel(Application application) {
        super(application);
        surveyRoomDao = DataBaseRoom.getInstance(application).surveyRoomDao();
        questionRoomDao = DataBaseRoom.getInstance(application).questionRoomDao();
    }

    public void createSurveys(Surveys surveys, @Nullable List<TextQuestion> textQuestionList,
                              @Nullable List<UniqueChoiceQuestion> uniqueChoiceQuestionList,
                              @Nullable List<MultipleChoiceQuestion> multipleChoiceQuestionList){
        new Thread(() -> surveys.setId(surveyRoomDao.insert(surveys))).start();

        assert textQuestionList != null;
        boolean hasError = false;
        for (TextQuestion textQuestion : textQuestionList){
            if (textQuestion.build()) {
                textQuestion.setSurveyId(surveys.getId());
                new Thread(() -> questionRoomDao.insertTextQuestion(textQuestion)).start();
            }else {
                hasError = true;
                break;
            }
        }
        assert uniqueChoiceQuestionList != null;
        for (UniqueChoiceQuestion uniqueChoiceQuestion : uniqueChoiceQuestionList){
            if (uniqueChoiceQuestion.build()) {
                uniqueChoiceQuestion.setSurveyId(surveys.getId());
                new Thread(() -> questionRoomDao.insertUniqueChoiceQuestion(uniqueChoiceQuestion)).start();
            }else {
                hasError = true;
                break;
            }
        }

        assert multipleChoiceQuestionList != null;
        for (MultipleChoiceQuestion multipleChoiceQuestion : multipleChoiceQuestionList){
            if (multipleChoiceQuestion.build()) {
                multipleChoiceQuestion.setSurveyId(surveys.getId());
                new Thread(() -> questionRoomDao.insertMultipleChoiceQuestion(multipleChoiceQuestion)).start();
            }else break;
        }
    }

}