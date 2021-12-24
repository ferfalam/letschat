package com.faridcodeur.letschat.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;

import java.util.List;

@Dao
public interface QuestionRoomDao {

    //TODO TextQuestion Requests
    @Query("SELECT * FROM textquestion")
    List<Surveys> findAllTextQuestion();

    @Query("SELECT * FROM textquestion WHERE id IN (:ids)")
    List<Surveys> loadAllTextQuestionByIds(int[] ids);

    @Query("SELECT * FROM textquestion WHERE surveyId = :surveyId")
    List<Surveys> findTextQuestionBySurveyId(int surveyId);

    @Insert
    void insertAllTextQuestion(TextQuestion... textQuestions);

    @Insert
    void insertTextQuestion(TextQuestion textQuestion);

    @Delete
    void deleteTextQuestion(TextQuestion textQuestion);

    @Update
    void updateTextQuestion(TextQuestion textQuestion);


    //TODO UniqueChoiceQuestion Requests
    @Query("SELECT * FROM uniquechoicequestion")
    List<Surveys> findAllUniqueChoiceQuestion();

    @Query("SELECT * FROM uniquechoicequestion WHERE id IN (:ids)")
    List<Surveys> loadAllUniqueChoiceQuestion(int[] ids);

    @Query("SELECT * FROM uniquechoicequestion WHERE surveyId = :surveyId")
    List<Surveys> findUniqueChoiceQuestionBySurveyId(int surveyId);

    @Insert
    void insertAllUniqueChoiceQuestion(UniqueChoiceQuestion... uniqueChoiceQuestions);

    @Insert
    void insertUniqueChoiceQuestion(UniqueChoiceQuestion uniqueChoiceQuestion);

    @Delete
    void deleteUniqueChoiceQuestion(UniqueChoiceQuestion uniqueChoiceQuestion);

    @Update
    void updateUniqueChoiceQuestion(UniqueChoiceQuestion uniqueChoiceQuestion);


    //TODO MultipleChoiceQuestion Requests
    @Query("SELECT * FROM multiplechoicequestion")
    List<Surveys> findAllMultipleChoiceQuestion();

    @Query("SELECT * FROM multiplechoicequestion WHERE id IN (:ids)")
    List<Surveys> loadAllMultipleChoiceQuestion(int[] ids);

    @Query("SELECT * FROM multiplechoicequestion WHERE surveyId = :surveyId")
    List<Surveys> findMultipleChoiceQuestionBySurveyId(int surveyId);

    @Insert
    void insertAllMultipleChoiceQuestion(MultipleChoiceQuestion... multipleChoiceQuestions);

    @Insert
    void insertMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion);

    @Delete
    void deleteMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion);

    @Update
    void updateMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion);
}
