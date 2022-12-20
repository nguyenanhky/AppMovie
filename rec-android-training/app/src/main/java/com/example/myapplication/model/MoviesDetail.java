package com.example.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

@Entity
public class MoviesDetail implements Comparable<MoviesDetail> {
    @PrimaryKey
    private int id;
    private String overview;
    private String poster_path;
    private String release_date;
    private String title;
    private double vote_average;


    public MoviesDetail(int id, String overview,String poster_path, String release_date, String title, double vote_average) {
        this.id = id;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public static boolean sortBy = true;

    @Override
    public int compareTo(MoviesDetail moviesDetail) {
        if(sortBy)
            return Double.compare(moviesDetail.vote_average, vote_average);
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long date1 = 0;
            long date2 = 0;
            try {
                date1 =  simpleDateFormat.parse(moviesDetail.release_date).getTime();
                date2 =  simpleDateFormat.parse(release_date).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return Long.compare(date1,date2);
        }
    }
}