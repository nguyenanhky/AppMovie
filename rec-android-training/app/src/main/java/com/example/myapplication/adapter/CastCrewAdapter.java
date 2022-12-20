package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemCastCrewBinding;
import com.example.myapplication.model.CastAndCrewDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CastCrewAdapter extends RecyclerView.Adapter<CastCrewAdapter.ViewHolder> {
    Context context;
    List<CastAndCrewDetail> list = new ArrayList<>();

    public CastCrewAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<CastAndCrewDetail> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCastCrewBinding binding =
                ItemCastCrewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new  ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemCastCrewBinding binding;
        public ViewHolder(@NonNull ItemCastCrewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position){
            CastAndCrewDetail castAndCrewDetail = list.get(position);
            String link = "http://image.tmdb.org/t/p/original" +castAndCrewDetail.getProfile_path();
            Picasso.with(context).load(link).into(binding.img);
            binding.txt.setText(castAndCrewDetail.getName());

        }
    }
}
