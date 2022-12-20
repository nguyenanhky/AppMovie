package com.example.myapplication.ui.setting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.SharedPreferences;
import com.example.myapplication.databinding.DialogCategoryBinding;
import com.example.myapplication.databinding.DialogRateFromBinding;
import com.example.myapplication.databinding.DialogSortByBinding;
import com.example.myapplication.databinding.DialogYearFromBinding;
import com.example.myapplication.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private ChangeFilter changeFilter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showData();

        binding.btnCategory.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(this.requireContext());
            DialogCategoryBinding dialogBinding = DialogCategoryBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            switch (SharedPreferences.getCategory(this.requireContext())){
                case SharedPreferences.POPULAR:
                    dialogBinding.radioPopular.setChecked(true);
                    break;
                case SharedPreferences.TOP_RATE:
                    dialogBinding.radioTopRate.setChecked(true);
                    break;
                case SharedPreferences.UPCOMING:
                    dialogBinding.radioUpcoming.setChecked(true);
                    break;
                case SharedPreferences.NOW_PLAYING:
                    dialogBinding.radioNowPlaying.setChecked(true);
                    break;
            }

            dialogBinding.btnCancel.setOnClickListener(view2 -> {
                dialog.dismiss();
            });

            dialogBinding.btnOk.setOnClickListener(view2 -> {
                if(dialogBinding.radioPopular.isChecked()){
                    SharedPreferences.setCategory(this.requireContext(), SharedPreferences.POPULAR);
                }
                else if(dialogBinding.radioTopRate.isChecked()){
                    SharedPreferences.setCategory(this.requireContext(), SharedPreferences.TOP_RATE);
                }
                else if(dialogBinding.radioUpcoming.isChecked()){
                    SharedPreferences.setCategory(this.requireContext(), SharedPreferences.UPCOMING);
                }
                else if(dialogBinding.radioNowPlaying.isChecked()){
                    SharedPreferences.setCategory(this.requireContext(), SharedPreferences.NOW_PLAYING);
                }
                showData();
                changeFilter.changeFilter();
                dialog.dismiss();
            });
            dialog.show();
        });

        binding.btnRateFrom.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(this.requireContext());
            DialogRateFromBinding dialogBinding = DialogRateFromBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            dialogBinding.seekBar.setMax(10);
            dialogBinding.seekBar.setProgress(SharedPreferences.getRateFrom(this.requireContext()));
            dialogBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    dialogBinding.txtRateFrom.setText(i+"");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            dialogBinding.btnCancel.setOnClickListener(view2 -> {
                dialog.dismiss();
            });
            dialogBinding.btnOk.setOnClickListener(view2 -> {
                SharedPreferences.setRateFrom(this.requireContext(), dialogBinding.seekBar.getProgress());
                showData();
                changeFilter.changeFilter();
                dialog.dismiss();
            });
            dialog.show();
        });

        binding.btnReleaseYear.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(this.requireContext());
            DialogYearFromBinding dialogBinding = DialogYearFromBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            dialogBinding.editTextYear.setText(SharedPreferences.getYearFrom(this.requireContext())+ "");
            dialogBinding.btnCancel.setOnClickListener(view2 -> {
                dialog.dismiss();
            });
            dialogBinding.btnOk.setOnClickListener(view2 -> {
                String year = dialogBinding.editTextYear.getText().toString();
                if(year.trim().isEmpty()){
                    Toast.makeText(this.requireContext(), R.string.year_blank,Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(year)<1000){
                    Toast.makeText(this.requireContext(), R.string.year_1000,Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences.setYearFrom(requireContext(), Integer.parseInt(year));
                    showData();
                    changeFilter.changeFilter();
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        binding.btnSortBy.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(this.requireContext());
            DialogSortByBinding dialogBinding = DialogSortByBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());

            switch (SharedPreferences.getSortBy(this.requireContext())){
                case SharedPreferences.SORT_BY_RATE:
                    dialogBinding.radioRating.setChecked(true);
                    break;
                case SharedPreferences.SORT_BY_RELEASE_DATE:
                    dialogBinding.radioReleaseDate.setChecked(true);
                    break;
            }

            dialogBinding.btnCancel.setOnClickListener(view2 -> {
                dialog.dismiss();
            });

            dialogBinding.btnOk.setOnClickListener(view2 -> {
                if(dialogBinding.radioReleaseDate.isChecked()){
                    SharedPreferences.setSortBy(this.requireContext(), SharedPreferences.SORT_BY_RELEASE_DATE);
                }
                else if(dialogBinding.radioRating.isChecked()){
                    SharedPreferences.setSortBy(this.requireContext(), SharedPreferences.SORT_BY_RATE);
                }
                showData();
                changeFilter.changeFilter();
                dialog.dismiss();
            });

            dialog.show();
        });
    }

    public void showData(){
        switch (SharedPreferences.getCategory(this.requireContext())){
            case SharedPreferences.POPULAR:
                binding.txtCategory.setText(R.string.popular_movies);
                break;
            case SharedPreferences.TOP_RATE:
                binding.txtCategory.setText(R.string.top_rated_movies);
                break;
            case SharedPreferences.UPCOMING:
                binding.txtCategory.setText(R.string.upcoming_movies);
                break;
            case SharedPreferences.NOW_PLAYING:
                binding.txtCategory.setText(R.string.now_playing_movies);
                break;
        }

        binding.txtRateFrom.setText(SharedPreferences.getRateFrom(requireContext())+"");
        binding.txtReleaseYear.setText(SharedPreferences.getYearFrom(requireContext())+"");

        switch (SharedPreferences.getSortBy(this.requireContext())){
            case SharedPreferences.SORT_BY_RATE:
                binding.txtSortBy.setText(R.string.rating_2);
                break;
            case SharedPreferences.SORT_BY_RELEASE_DATE:
                binding.txtSortBy.setText(R.string.release_date_2);
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        changeFilter = (ChangeFilter) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface ChangeFilter{
        void changeFilter();
    }
}