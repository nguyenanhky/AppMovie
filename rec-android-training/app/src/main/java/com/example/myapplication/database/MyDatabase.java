package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.model.Alarm;
import com.example.myapplication.model.MoviesDetail;

@Database(entities = {MoviesDetail.class, Alarm.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase myDatabase;

    public static final String DATABASE_NAME = "database";

    public abstract MyDao myDao();

    public static MyDatabase getInstance(Context context) {
        if (myDatabase == null) {
            myDatabase = Room.databaseBuilder(context, MyDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return myDatabase;
    }
}
