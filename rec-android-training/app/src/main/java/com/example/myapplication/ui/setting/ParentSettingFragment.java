package com.example.myapplication.ui.setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentParentSettingBinding;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.ui.movies.MovieDetailFragment;

public class ParentSettingFragment extends Fragment {

    FragmentParentSettingBinding binding;
    SettingFragment settingFragment;
    NotificationFragment notificationFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentSettingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingFragment = new SettingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, settingFragment).commit();
    }

    public void showData(){
        settingFragment.showData();
    }

    public void goToNotification(){
        if(!(getChildFragmentManager().findFragmentById(R.id.frameLayout) instanceof ParentSettingFragment)){
            notificationFragment = NotificationFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, notificationFragment).commit();
            transaction.addToBackStack(null);
        }
    }
}