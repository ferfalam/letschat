package com.faridcodeur.letschat.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridcodeur.letschat.entities.Surveys;

import java.util.List;

@Dao
public interface SurveyRoomDao {

    @Query("SELECT * FROM surveys")
    List<Surveys> findAll();

    @Query("SELECT * FROM surveys WHERE id IN (:surveysIds)")
    List<Surveys> loadAllByIds(int[] surveysIds);

    @Query("SELECT * FROM surveys WHERE title LIKE :title AND " + "description LIKE :description")
    List<Surveys> findByTitle(String title, String description);

    @Insert
    void insertAll(Surveys... products);

    @Insert
    default int insert(Surveys surveys){return surveys.getId();}

    @Delete
    void delete(Surveys surveys);

    @Update
    void update(Surveys surveys);
}
