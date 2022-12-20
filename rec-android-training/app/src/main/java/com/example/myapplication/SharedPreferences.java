package com.example.myapplication;

import android.content.Context;

public class SharedPreferences {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String NAME = "NAME";
    private static final String MAIL = "MAIL";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String SEX = "SEX";
    private static final String AVATAR = "AVATAR";

    public static void saveAvatar(Context context, String avatar) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AVATAR, avatar);
        editor.apply();
    }

    public static void saveName(Context context, String name) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public static void saveMail(Context context, String mail) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MAIL, mail);
        editor.apply();
    }

    public static void saveBirthday(Context context, String time) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(BIRTHDAY, time);
        editor.apply();
    }

    public static void saveSex(Context context, String sex) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SEX, sex);
        editor.apply();
    }

    public static String getAvatar(Context context) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(AVATAR, "");
    }

    public static String getName(Context context) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(NAME, "");
    }

    public static String getMail(Context context) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(MAIL, "");
    }

    public static String getBirthday(Context context) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(BIRTHDAY, "");
    }

    public static String getSex(Context context) {
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(SEX, "");
    }

    public static final int POPULAR = 1;
    public static final int TOP_RATE = 2;
    public static final int UPCOMING = 3;
    public static final int NOW_PLAYING = 4;
    private static final String CATEGORY = "category";

    public static void setCategory(Context context, int i){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CATEGORY, i);
        editor.apply();
    }

    public static int getCategory(Context context){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(CATEGORY, 1);
    }


    private static final String RATE_FROM = "rate from";
    public static void setRateFrom(Context context, int i){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(RATE_FROM, i);
        editor.apply();
    }

    public static int getRateFrom(Context context){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(RATE_FROM, 0);
    }

    private static final String YEAR_FROM = "year from";
    public static void setYearFrom(Context context, int i){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(YEAR_FROM, i);
        editor.apply();
    }

    public static int getYearFrom(Context context){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(YEAR_FROM, 2015);
    }


    public static final int SORT_BY_RATE = 1;
    public static final int SORT_BY_RELEASE_DATE = 2;
    private static final String SORT_BY = "sort by";
    public static void setSortBy(Context context, int i){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SORT_BY, i);
        editor.apply();
    }

    public static int getSortBy(Context context){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(SORT_BY, 1);
    }
}
