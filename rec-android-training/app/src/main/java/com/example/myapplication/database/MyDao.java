package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.model.Alarm;
import com.example.myapplication.model.MoviesDetail;

import java.util.List;

@Dao
public interface MyDao {
    @Query("SELECT * FROM MoviesDetail")
    List<MoviesDetail> getAll();

    @Query("SELECT * FROM MoviesDetail WHERE id IN (:id)")
    MoviesDetail loadById(int id);

    @Insert
    void insertAll(MoviesDetail... moviesDetail);

    @Delete
    void delete(MoviesDetail... moviesDetail);

    @Query("SELECT * FROM Alarm ORDER BY time DESC")
    List<Alarm> getAllAlarm();

    @Insert
    void insertAll(Alarm... alarms);

    @Delete
    void delete(Alarm... alarms);
}
