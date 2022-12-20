package com.example.myapplication.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.AlarmBroadcastReceiver;

@Entity
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String linkThum;
    private String title1;
    private String title2;
    private long time;

    public Alarm(int id, String linkThum, String title1, String title2, long time) {
        this.id = id;
        this.linkThum = linkThum;
        this.title1 = title1;
        this.title2 = title2;
        this.time = time;
    }

    public String getLinkThum() {
        return linkThum;
    }

    public void setLinkThum(String linkThum) {
        this.linkThum = linkThum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSchedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        intent.putExtra("TITLE1", title1);
        intent.putExtra("TITLE2", title2);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) time, intent, 0);

        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                time,
                alarmPendingIntent
        );
    }

    public void cancelSchedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) time, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
    }
}
