package com.example.myapplication.ui.favourite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.MoviesAdapter;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.DialogDeleteFavouriteBinding;
import com.example.myapplication.databinding.FragmentFavouriteBinding;
import com.example.myapplication.model.MoviesDetail;
import com.example.myapplication.ui.movies.MoviesFragment;

import java.util.List;

public class FavouriteFragment extends Fragment implements MoviesAdapter.OnMoviesClick {
    MoviesAdapter moviesAdapter;
    private FragmentFavouriteBinding binding;
    private MyDatabase myDatabase;
    DeleteFavorite deleteFavorite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        myDatabase = MyDatabase.getInstance(this.requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRcv();
        loadFavourite();
    }

    void setUpRcv(){
        moviesAdapter = new MoviesAdapter(FavouriteFragment.this.requireContext(), false, this);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(FavouriteFragment.this.requireContext(), RecyclerView.VERTICAL, false);
        binding.rcv.setLayoutManager(layoutManager);
        binding.rcv.setAdapter(moviesAdapter);
    }

    public int loadFavourite(){
        List<MoviesDetail> list = myDatabase.myDao().getAll();
        moviesAdapter.setData(list);
        binding.progress.setVisibility(View.GONE);
        return moviesAdapter.getListMovies().size();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int id, String title) {

    }

    @Override
    public void onLongClick(MoviesDetail moviesDetail) {
        Dialog dialog = new Dialog(this.requireContext());
        DialogDeleteFavouriteBinding dialogBinding = DialogDeleteFavouriteBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();
        dialogBinding.txtCancel.setOnClickListener(view -> dialog.dismiss());
        dialogBinding.txtDelete.setOnClickListener(view -> {
            myDatabase.myDao().delete(moviesDetail);
            deleteFavorite.delete(moviesDetail.getId());
            dialog.dismiss();
        });
    }

    public void search(String query){
        moviesAdapter.getFilter().filter(query);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        deleteFavorite = (DeleteFavorite) context;
    }


    @Override
    public void onStarClick(int id) {
        deleteFavorite.delete(id);
    }

    public interface DeleteFavorite{
        void delete(int id);
    }
}