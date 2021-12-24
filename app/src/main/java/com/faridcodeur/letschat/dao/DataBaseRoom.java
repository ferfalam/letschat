package com.faridcodeur.letschat.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.faridcodeur.letschat.entities.Surveys;
import com.faridcodeur.letschat.survey.model.MultipleChoiceQuestion;
import com.faridcodeur.letschat.survey.model.TextQuestion;
import com.faridcodeur.letschat.survey.model.UniqueChoiceQuestion;

@Database(entities = {Surveys.class, TextQuestion.class, UniqueChoiceQuestion.class, MultipleChoiceQuestion.class}, version = 1)
public abstract class DataBaseRoom extends RoomDatabase {

    private static DataBaseRoom dataBaseRoom;

    public static DataBaseRoom getInstance(Context context) {
        if(dataBaseRoom==null) {
            dataBaseRoom = Room.databaseBuilder(context, DataBaseRoom.class, "project:db").build();
        }
        return dataBaseRoom;
    }

    public abstract SurveyRoomDao surveyRoomDao();

    public abstract QuestionRoomDao questionRoomDao();

}
