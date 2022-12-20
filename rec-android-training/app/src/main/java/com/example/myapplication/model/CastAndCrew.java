package com.example.myapplication.model;

import java.util.ArrayList;

public class CastAndCrew {
    private int id;
    private ArrayList<CastAndCrewDetail> cast;
    private ArrayList<CastAndCrewDetail> crew;

    public CastAndCrew(int id, ArrayList<CastAndCrewDetail> castAndCrewDetail, ArrayList<CastAndCrewDetail> crew) {
        this.id = id;
        this.cast = castAndCrewDetail;
        this.crew = crew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<CastAndCrewDetail> getCast() {
        return cast;
    }

    public void setCast(ArrayList<CastAndCrewDetail> castAndCrewDetail) {
        this.cast = castAndCrewDetail;
    }

    public ArrayList<CastAndCrewDetail> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<CastAndCrewDetail> crew) {
        this.crew = crew;
    }
}
