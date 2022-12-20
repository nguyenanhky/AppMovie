package com.example.myapplication.ui.movies;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.SharedPreferences;
import com.example.myapplication.adapter.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RestClient;
import com.example.myapplication.databinding.FragmentMoviesBinding;
import com.example.myapplication.model.ListMovies;
import com.example.myapplication.model.MoviesDetail;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviesFragment extends Fragment implements MoviesAdapter.OnMoviesClick {

    private FragmentMoviesBinding binding;
    ApiService apiService = RestClient.createService(ApiService.class);
    int page = 1;
    boolean isGrid = false;
    MoviesAdapter moviesAdapter;
    boolean isLoading = false;
    SendToParent sendToParent = null;
    StarClick starClick;
    int positionNow = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentMoviesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        try {
            sendToParent = (SendToParent) getParentFragment();
        }catch (ClassCastException e){
            e.printStackTrace();
            sendToParent = null;
        }
        setUpRcv();
        loadMore(page);
        binding.rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(isGrid){
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == moviesAdapter.getListMovies().size() - 1) {
                            page++;
                            loadMore(page);
                        }
                    }else {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == moviesAdapter.getListMovies().size() - 1) {
                            page++;
                            loadMore(page);
                        }
                    }
                }
            }
        });
    }

    public void setUpRcv(){
        RecyclerView.LayoutManager l = binding.rcv.getLayoutManager();
        if(l!= null)
            positionNow = ((LinearLayoutManager)l).findFirstVisibleItemPosition();
        RecyclerView.LayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(MoviesFragment.this.requireContext(), 2, RecyclerView.VERTICAL, false);
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position != moviesAdapter.getListMovies().size() - 1)
                        return 1;
                    else
                        return 2;
                }
            });
        }else {
            layoutManager = new LinearLayoutManager(MoviesFragment.this.requireContext(), RecyclerView.VERTICAL, false);
        }
        moviesAdapter = new MoviesAdapter(MoviesFragment.this.requireContext(), isGrid, this);
        binding.rcv.setLayoutManager(layoutManager);
        binding.rcv.setAdapter(moviesAdapter);
        layoutManager.scrollToPosition(positionNow);
    }



    public void changeFilter(){
        RecyclerView.LayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(MoviesFragment.this.requireContext(), 2, RecyclerView.VERTICAL, false);

        }else {
            layoutManager = new LinearLayoutManager(MoviesFragment.this.requireContext(), RecyclerView.VERTICAL, false);
        }
        moviesAdapter = new MoviesAdapter(MoviesFragment.this.requireContext(), isGrid, this);
        binding.rcv.setLayoutManager(layoutManager);
        binding.rcv.setAdapter(moviesAdapter);
        page = 1;
        loadMore(page);
    }

    void loadMore(int page){
        isLoading = true;
        moviesAdapter.startLoadMore();
        new Handler().postDelayed(()->{
            switch (SharedPreferences.getCategory(this.requireContext())){
                case SharedPreferences.POPULAR:
                    apiService.getMoviesListPopular(page).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onNext, this::error, this::complete);
                    break;
                case SharedPreferences.TOP_RATE:
                    apiService.getMoviesListTopRated(page).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onNext, this::error, this::complete);
                    break;
                case SharedPreferences.UPCOMING:
                    apiService.getMoviesListUpcoming(page).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onNext, this::error, this::complete);
                    break;
                case SharedPreferences.NOW_PLAYING:
                    apiService.getMoviesListNowPlaying(page).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onNext, this::error, this::complete);
                    break;
            }
        },500);

    }

    void onNext(ListMovies listMovies){
        isLoading = false;
        moviesAdapter.addData(listMovies.getResults());
    }

    void complete(){

    }

    void error(Throwable e){
        isLoading = false;
        e.printStackTrace();
    }

    public void changeGrid(boolean b){
        isGrid = b;
        List<MoviesDetail> list = moviesAdapter.getListMovies();
        setUpRcv();
        moviesAdapter.addData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int id, String title) {
        if(sendToParent!=null)
            sendToParent.send(id, title);
    }

    @Override
    public void onLongClick(MoviesDetail moviesDetail) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        starClick = (StarClick) context;
    }

    @Override
    public void onStarClick(int id) {
        if(starClick!= null)
            starClick.onStarClick();
    }

    public interface SendToParent{
        void send(int id, String title);
    }

    public interface StarClick{
        void onStarClick();
    }

    public void updateWhenDeleteFavorite(int id){
        for (int i = 0; i < moviesAdapter.getListMovies().size(); i++) {
            if(moviesAdapter.getListMovies().get(i).getId() == id){
                moviesAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}