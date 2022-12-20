package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SharedPreferences;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.ItemLoadingBinding;
import com.example.myapplication.databinding.ItemMoviesBinding;
import com.example.myapplication.databinding.ItemMoviesGridBinding;
import com.example.myapplication.model.MoviesDetail;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<MoviesDetail> listMoviesOld = new ArrayList<>();
    private List<MoviesDetail> listMovies = new ArrayList<>();
    private boolean isGrid;
    private OnMoviesClick onMoviesClick;
    private MyDatabase myDatabase;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public List<MoviesDetail> getListMovies() {
        return listMovies;
    }

    public MoviesAdapter(Context context, boolean isGrid, OnMoviesClick onMoviesClick) {
        this.context = context;
        this.isGrid = isGrid;
        this.onMoviesClick = onMoviesClick;
        myDatabase = MyDatabase.getInstance(context);
    }

    public void startLoadMore(){
        this.listMovies.add(null);
        notifyItemInserted(listMovies.size()-1);
    }

    public void addData(List<MoviesDetail> listMovies){
        this.listMovies.remove(null);
        int minYear = SharedPreferences.getYearFrom(context);
        int minRate = SharedPreferences.getRateFrom(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<MoviesDetail> listFilter = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (MoviesDetail moviesDetail: listMovies){
            Date date;
            try {
                date = simpleDateFormat.parse(moviesDetail.getRelease_date());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(moviesDetail.getVote_average() >= minRate && calendar.get(Calendar.YEAR)>=minYear){
                listFilter.add(moviesDetail);
            }
        }

        MoviesDetail.sortBy = SharedPreferences.getSortBy(context) == SharedPreferences.SORT_BY_RATE;
        Collections.sort(listFilter);
        this.listMovies.addAll(listFilter);
        notifyDataSetChanged();
    }

    public void setData(List<MoviesDetail> listMovies){
        listMoviesOld = listMovies;
        this.listMovies= listMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_LOADING){
            ItemLoadingBinding binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolderLoad(binding);
        }else {
            if(isGrid){
                ItemMoviesGridBinding binding = ItemMoviesGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ViewHolderGrid(binding);
            }else {
                ItemMoviesBinding binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ViewHolder(binding);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderLoad){

        }else {
            if(isGrid){
                ((ViewHolderGrid)holder).bind(position);
            }else {
                ((ViewHolder)holder).bind(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listMovies = listMoviesOld;
                } else {
                    List<MoviesDetail> filteredList = new ArrayList<>();
                    for (MoviesDetail moviesDetail : listMoviesOld) {

                        if (moviesDetail.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(moviesDetail);
                        }
                    }

                    listMovies = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listMovies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listMovies = (ArrayList<MoviesDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return listMovies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM ;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ItemMoviesBinding binding;
        public ViewHolder(ItemMoviesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position){
            MoviesDetail moviesDetail = listMovies.get(position);
            binding.txtNameMovies.setText(moviesDetail.getTitle());
            binding.txtRatingDetail.setText(moviesDetail.getVote_average()+"/10");
            binding.txtReleaseDateDetail.setText(moviesDetail.getRelease_date());
            binding.txtOverviewDetail.setText(moviesDetail.getOverview());
            String link = "http://image.tmdb.org/t/p/original" +moviesDetail.getPoster_path();
            Picasso.with(context).load(link).into(binding.imgMovies);

            boolean isFavourite = checkFavourite(moviesDetail.getId());
            if(isFavourite){
                binding.imgStar.setImageResource(R.mipmap.ic_start_selected);
            }else {
                binding.imgStar.setImageResource(R.mipmap.ic_star_border_black);
            }

            binding.imgStar.setOnClickListener(view -> {
                if(isFavourite){
                    myDatabase.myDao().delete(moviesDetail);
                }else {
                    myDatabase.myDao().insertAll(moviesDetail);
                }
                notifyItemChanged(position);
                onMoviesClick.onStarClick(moviesDetail.getId());
            });

            binding.getRoot().setOnLongClickListener(view -> {
                onMoviesClick.onLongClick(moviesDetail);
                return true;
            });

            binding.getRoot().setOnClickListener(view -> {
                onMoviesClick.onClick(moviesDetail.getId(), moviesDetail.getTitle());
            });
        }
    }

    class ViewHolderGrid extends RecyclerView.ViewHolder{
        private ItemMoviesGridBinding binding;
        public ViewHolderGrid(ItemMoviesGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(int position){
            MoviesDetail moviesDetail = listMovies.get(position);
            binding.txtNameMovies.setText(moviesDetail.getTitle());
            String link = "http://image.tmdb.org/t/p/original" +moviesDetail.getPoster_path();
            Picasso.with(context).load(link).into(binding.imgMovies);

            binding.getRoot().setOnClickListener(view -> {
                onMoviesClick.onClick(moviesDetail.getId(), moviesDetail.getTitle());
            });

        }
    }

    class ViewHolderLoad extends RecyclerView.ViewHolder{
        public ViewHolderLoad(ItemLoadingBinding binding) {
            super(binding.getRoot());
        }
    }

    boolean checkFavourite(int id){
        MoviesDetail moviesDetail = myDatabase.myDao().loadById(id);
        return moviesDetail!=null;
    }

    public interface OnMoviesClick{
        void onClick(int id, String title);
        void onLongClick(MoviesDetail moviesDetail);
        void onStarClick(int id);
    }
}
