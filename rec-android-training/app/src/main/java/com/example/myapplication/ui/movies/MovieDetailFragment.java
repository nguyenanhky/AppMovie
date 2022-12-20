package com.example.myapplication.ui.movies;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CastCrewAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RestClient;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.FragmentMovieDetailBinding;
import com.example.myapplication.model.Alarm;
import com.example.myapplication.model.CastAndCrew;
import com.example.myapplication.model.CastAndCrewDetail;
import com.example.myapplication.model.MoviesDetail;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailFragment extends Fragment {

    private static final String ID_PARAM = "id_param";
    private int id = -1;
    private FragmentMovieDetailBinding binding;
    ApiService apiService = RestClient.createService(ApiService.class);
    CastCrewAdapter adapter;
    MyDatabase myDatabase;
    MoviesDetail moviesDetail;
    ClickStarInMovieDetailFragment clickStar;

    public static MovieDetailFragment newInstance(int idParam) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ID_PARAM, idParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDatabase = MyDatabase.getInstance(this.requireContext());
        if(id!=-1){
            adapter = new CastCrewAdapter(this.requireContext());
            binding.rcv.setLayoutManager(new LinearLayoutManager(this.requireContext(), RecyclerView.HORIZONTAL, false));
            binding.rcv.setAdapter(adapter);
            apiService.getMoviesDetail(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MoviesDetail>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull MoviesDetail moviesDetail) {
                            if(getContext()!= null){
                                MovieDetailFragment.this.moviesDetail = moviesDetail;
                                String link = "http://image.tmdb.org/t/p/original" +moviesDetail.getPoster_path();
                                Picasso.with(MovieDetailFragment.this.requireContext()).load(link).into(binding.imgMovies);
                                binding.txtOverviewDetail.setText(moviesDetail.getOverview());
                                binding.txtRatingDetail.setText(moviesDetail.getVote_average()+"/10");
                                binding.txtReleaseDateDetail.setText(moviesDetail.getRelease_date());
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            apiService.getCastAndCrew(id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CastAndCrew>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull CastAndCrew castAndCrew) {
                            if(getContext()!= null){
                                List<CastAndCrewDetail> list = new ArrayList<>();
                                list.addAll(castAndCrew.getCast());
                                list.addAll(castAndCrew.getCrew());
                                adapter.setDate(list);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            showStar(id);

            binding.imgStar.setOnClickListener(view1 -> {
                if(myDatabase.myDao().loadById(id) != null){
                    myDatabase.myDao().delete(moviesDetail);
                }else
                    myDatabase.myDao().insertAll(moviesDetail);
                clickStar.onClickStarInMovieDetailFragment(id);
            });

            binding.btnReminder.setOnClickListener(view1 -> {

                Calendar calendar = Calendar.getInstance();
                int selectedYear = calendar.get(Calendar.YEAR);
                int selectedMonth = calendar.get(Calendar.MONTH);
                int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int lastSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
                int lastSelectedMinute = calendar.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        (datePicker, year,
                         monthOfYear, dayOfMonth) -> {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                                    (timePicker, hourOfDay, minute) -> {
                                        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                        String title2 = "Year: "+ moviesDetail.getRelease_date().substring(0, 4)+ "Rate: "+moviesDetail.getVote_average()+"/10";
                                        Alarm alarm = new Alarm(0, moviesDetail.getPoster_path(), moviesDetail.getTitle(),title2, calendar.getTimeInMillis());
                                        alarm.setSchedule(requireContext());
                                        myDatabase.myDao().insertAll(alarm);
                                    }, lastSelectedHour, lastSelectedMinute, false);
                            timePickerDialog.show();
                        }, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickStar = (ClickStarInMovieDetailFragment) context;
    }

    void showStar(int id){
        if(myDatabase.myDao().loadById(id) != null){
            binding.imgStar.setImageResource(R.mipmap.ic_start_selected);
        }else {
            binding.imgStar.setImageResource(R.mipmap.ic_star_border_black);
        }
    }

    public interface ClickStarInMovieDetailFragment{
        void onClickStarInMovieDetailFragment(int id);
    }

}