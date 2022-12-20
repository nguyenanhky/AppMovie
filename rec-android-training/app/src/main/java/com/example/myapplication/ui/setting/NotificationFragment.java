package com.example.myapplication.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.AlarmBroadcastReceiver;
import com.example.myapplication.R;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.FragmentNotificationBinding;
import com.example.myapplication.model.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    MyDatabase myDatabase;
    NotificationAdapter notificationAdapter;

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDatabase = MyDatabase.getInstance(requireContext());
        notificationAdapter = new NotificationAdapter(requireContext(), myDatabase);
        binding.rcv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        binding.rcv.setAdapter(notificationAdapter);
        setData();
    }

    void setData(){
        List<Alarm> alarms = myDatabase.myDao().getAllAlarm();
        Calendar calendar = Calendar.getInstance();
        List<Alarm> alarmList = new ArrayList<>();
        for (Alarm alarm: alarms){
            if(calendar.getTimeInMillis()>= alarm.getTime())
                myDatabase.myDao().delete(alarm);
            else
                alarmList.add(alarm);
        }
        notificationAdapter.setData(alarmList);
    }
}