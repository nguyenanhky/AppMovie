package com.example.myapplication.ui.movies;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentParentMoviesBinding;
import com.example.myapplication.model.MoviesDetail;

import java.util.List;


public class ParentMoviesFragment extends Fragment implements MoviesFragment.SendToParent {

    private FragmentParentMoviesBinding binding;
    public MoviesFragment moviesFragment;
    MovieDetailFragment movieDetailFragment;
    GoToMoviesDetail goToMoviesDetail;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentMoviesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesFragment = new MoviesFragment();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, moviesFragment).commit();

    }

    public void changeGrid(boolean b){
        moviesFragment.changeGrid(b);
    }

    @Override
    public void send(int id, String title) {
        movieDetailFragment = MovieDetailFragment.newInstance(id);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, movieDetailFragment).commit();
        transaction.addToBackStack(null);
        goToMoviesDetail.goTo(title);
    }

    public void updateWhenDeleteFavourite(int id){
        Fragment fragmentNow = getChildFragmentManager().findFragmentById(R.id.frameLayout);
        moviesFragment.updateWhenDeleteFavorite(id);
        if(fragmentNow instanceof MovieDetailFragment)
            movieDetailFragment.showStar(id);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        goToMoviesDetail = (GoToMoviesDetail) context;
    }

    public interface GoToMoviesDetail{
            void goTo(String title);
    }
}