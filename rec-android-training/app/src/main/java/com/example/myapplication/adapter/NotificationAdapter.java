package com.example.myapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.ItemNotificationBinding;
import com.example.myapplication.model.Alarm;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Alarm> alarmList = new ArrayList<>();
    Context context;
    MyDatabase myDatabase;

    public NotificationAdapter(Context context, MyDatabase myDatabase) {
        this.context = context;
        this.myDatabase = myDatabase;
    }

    public void setData(List<Alarm> alarmList){
        this.alarmList = alarmList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemNotificationBinding binding;
        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position){
            Alarm alarm = alarmList.get(position);
            String link = "http://image.tmdb.org/t/p/original" +alarm.getLinkThum();
            Picasso.with(context).load(link).into(binding.img);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            binding.time.setText(simpleDateFormat.format(new Date(alarm.getTime())));
            String title1 = alarm.getTitle1();
            String title2 = alarm.getTitle2();
            title2 = title2.replace("Year: ","-");
            title2 = title2.replace("Rate: ","-");
            binding.title.setText(title1 + title2);

            binding.imgArrow.setOnClickListener(view -> {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete_notifi);
                dialog.findViewById(R.id.btnDelete).setOnClickListener(view1->{
                    dialog.dismiss();
                    Dialog dialog1 = new Dialog(context);
                    dialog1.setContentView(R.layout.dialog_delete_notifi_2);
                    dialog1.findViewById(R.id.btnNo).setOnClickListener(view2 -> dialog1.dismiss());
                    dialog1.findViewById(R.id.btnYes).setOnClickListener(view2 -> {
                        dialog1.dismiss();
                        myDatabase.myDao().delete(alarm);
                        alarmList.remove(position);
                        notifyItemRemoved(position);
                        alarm.cancelSchedule(context);
                    });
                    dialog1.show();
                });
                dialog.findViewById(R.id.btnCancel).setOnClickListener(view1 -> dialog.dismiss());
                dialog.show();
            });
        }
    }

}
