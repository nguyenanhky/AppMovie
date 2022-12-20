package com.example.myapplication.model;

public class CastAndCrewDetail {
    private String name;
    private String profile_path;


    public CastAndCrewDetail(String name, String profile_path) {

        this.name = name;

        this.profile_path = profile_path;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }


}

