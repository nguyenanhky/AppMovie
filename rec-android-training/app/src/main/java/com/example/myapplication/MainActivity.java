package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.database.MyDatabase;
import com.example.myapplication.databinding.ItemNotiBinding;
import com.example.myapplication.model.Alarm;
import com.example.myapplication.ui.about.AboutFragment;
import com.example.myapplication.ui.favourite.FavouriteFragment;
import com.example.myapplication.ui.movies.MovieDetailFragment;
import com.example.myapplication.ui.movies.MoviesFragment;
import com.example.myapplication.ui.movies.ParentMoviesFragment;
import com.example.myapplication.ui.setting.ParentSettingFragment;
import com.example.myapplication.ui.setting.SettingFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        MoviesFragment.StarClick, FavouriteFragment.DeleteFavorite,
        MovieDetailFragment.ClickStarInMovieDetailFragment,
        ParentMoviesFragment.GoToMoviesDetail,
        SettingFragment.ChangeFilter{
    boolean isGrid = false;
    private ActivityMainBinding binding;
    private ParentMoviesFragment parentMoviesFragment;
    private FavouriteFragment favouriteFragment;
    private ParentSettingFragment parentSettingFragment;
    private TextView txtCount;
    private MyDatabase myDatabase;
    private MenuItem itemGrid, itemSearch;
    SearchView searchView;
    private boolean animationPlay = false;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    showProfile();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myDatabase = MyDatabase.getInstance(this);
        setSupportActionBar(binding.appBarMain.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        showProfile();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        parentMoviesFragment = new ParentMoviesFragment();
        favouriteFragment = new FavouriteFragment();
        parentSettingFragment = new ParentSettingFragment();
        AboutFragment aboutFragment = new AboutFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(parentMoviesFragment);
        fragmentList.add(favouriteFragment);
        fragmentList.add(parentSettingFragment);
        fragmentList.add(aboutFragment);
        viewPagerAdapter.setFragmentList(fragmentList);
        binding.appBarMain.content.viewPager.setAdapter(viewPagerAdapter);
        binding.appBarMain.content.viewPager.setOffscreenPageLimit(3);
        new TabLayoutMediator(binding.appBarMain.content.tabLayout, binding.appBarMain.content.viewPager,
                (tab, position) -> {
                    TabLayout.Tab view = tab.setCustomView(R.layout.tab_item);
                    TextView txtTitle = view.view.findViewById(R.id.txtTitle);
                    ImageView imgIcon = view.view.findViewById(R.id.imgIcon);
                    TextView txtCount = view.view.findViewById(R.id.txtItemCount);
                    switch (position) {
                        case 0:
                            txtTitle.setText(R.string.movies);
                            imgIcon.setImageResource(R.mipmap.ic_home);
                            break;
                        case 1:
                            txtTitle.setText(R.string.favourite);
                            imgIcon.setImageResource(R.mipmap.ic_favorite);
                            txtCount.setVisibility(View.VISIBLE);
                            this.txtCount = txtCount;
                            break;
                        case 2:
                            txtTitle.setText(R.string.action_settings);
                            imgIcon.setImageResource(R.mipmap.ic_settings);
                            break;
                        case 3:
                            txtTitle.setText(R.string.about);
                            imgIcon.setImageResource(R.mipmap.ic_info);
                            break;
                    }
                }
        ).attach();

        binding.appBarMain.content.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int count = parentMoviesFragment.getChildFragmentManager().getBackStackEntryCount();
                int count2 = parentSettingFragment.getChildFragmentManager().getBackStackEntryCount();
                switch (position) {
                    case 0:
                        if (itemSearch != null)
                            itemSearch.setVisible(false);
                        if (searchView != null) {
                            searchView.setQuery("", true);
                            searchView.setIconified(true);
                        }
                        if (itemGrid != null)
                            itemGrid.setVisible(true);
                        changeTitleInMovies();
                        if(count2!=0)
                            parentSettingFragment.getChildFragmentManager().popBackStack();
                        break;
                    case 1:
                        if (itemSearch != null)
                            itemSearch.setVisible(true);
                        if (itemGrid != null)
                            itemGrid.setVisible(false);

                        binding.appBarMain.txtTitle.setText(R.string.favourite);
                        binding.appBarMain.imgArrow.setVisibility(View.GONE);
                        binding.appBarMain.content.layoutArrow.setVisibility(View.GONE);
                        if (count != 0) {
                            parentMoviesFragment.getChildFragmentManager().popBackStack();
                        }
                        if(count2!=0)
                            parentSettingFragment.getChildFragmentManager().popBackStack();
                        break;
                    case 2:
                        if (itemSearch != null)
                            itemSearch.setVisible(false);
                        if (searchView != null) {
                            searchView.setQuery("", true);
                            searchView.setIconified(true);
                        }
                        if (itemGrid != null)
                            itemGrid.setVisible(false);
                        binding.appBarMain.txtTitle.setText(R.string.action_settings);
                        binding.appBarMain.content.layoutArrow.setVisibility(View.GONE);
                        binding.appBarMain.imgArrow.setVisibility(View.GONE);
                        if (count != 0) {
                            parentMoviesFragment.getChildFragmentManager().popBackStack();
                        }
                        break;
                    case 3:
                        if (itemSearch != null)
                            itemSearch.setVisible(false);
                        if (searchView != null) {
                            searchView.setQuery("", true);
                            searchView.setIconified(true);
                        }
                        if (itemGrid != null)
                            itemGrid.setVisible(false);
                        binding.appBarMain.txtTitle.setText(R.string.about);
                        binding.appBarMain.content.layoutArrow.setVisibility(View.GONE);
                        binding.appBarMain.imgArrow.setVisibility(View.GONE);
                        if (count != 0) {
                            parentMoviesFragment.getChildFragmentManager().popBackStack();
                        }
                        if(count2!=0)
                            parentSettingFragment.getChildFragmentManager().popBackStack();
                        break;
                }
            }
        });

        binding.btnEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            someActivityResultLauncher.launch(intent);
        });

        int count = myDatabase.myDao().getAll().size();
        txtCount.setText(count + "");

        binding.appBarMain.imgDraw.setOnClickListener(view -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
            showNoti();
        });

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.appBarMain.content.layoutArrow.setVisibility(View.GONE);
                binding.appBarMain.imgArrow.setImageResource(R.mipmap.ic_arrow_drop_down);
                animationPlay = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationPlay = false;
                binding.appBarMain.imgArrow.setImageResource(R.mipmap.ic_arrow_drop_up);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.appBarMain.btnArrow.setOnClickListener(view -> {
            if(binding.appBarMain.content.viewPager.getCurrentItem() == 0)
                if (!animationPlay) {
                    if(binding.appBarMain.content.layoutArrow.isShown()){
                        binding.appBarMain.content.layoutArrow.startAnimation(slideUp);
                    }else {
                        binding.appBarMain.content.layoutArrow.setVisibility(View.VISIBLE);
                        binding.appBarMain.content.layoutArrow.startAnimation(slideDown);
                    }
                    animationPlay = true;
                }
        });

        binding.appBarMain.content.btnPoplarMovies.setOnClickListener(view -> {
            if(!animationPlay){
                SharedPreferences.setCategory(this, SharedPreferences.POPULAR);
                parentMoviesFragment.moviesFragment.changeFilter();
                binding.appBarMain.txtTitle.setText(R.string.popular_movies);
                binding.appBarMain.content.layoutArrow.startAnimation(slideUp);
                parentSettingFragment.showData();
            }
        });
        binding.appBarMain.content.btnTopRate.setOnClickListener(view -> {
            if(!animationPlay){
                SharedPreferences.setCategory(this, SharedPreferences.TOP_RATE);
                parentMoviesFragment.moviesFragment.changeFilter();
                binding.appBarMain.txtTitle.setText(R.string.top_rated_movies);
                binding.appBarMain.content.layoutArrow.startAnimation(slideUp);
                parentSettingFragment.showData();
            }
        });
        binding.appBarMain.content.btnUpComing.setOnClickListener(view -> {
            if(!animationPlay){
                SharedPreferences.setCategory(this, SharedPreferences.UPCOMING);
                parentMoviesFragment.moviesFragment.changeFilter();
                binding.appBarMain.txtTitle.setText(R.string.upcoming_movies);
                binding.appBarMain.content.layoutArrow.startAnimation(slideUp);
                parentSettingFragment.showData();
            }
        });
        binding.appBarMain.content.btnNowPlay.setOnClickListener(view -> {
            if(!animationPlay){
                SharedPreferences.setCategory(this, SharedPreferences.NOW_PLAYING);
                parentMoviesFragment.moviesFragment.changeFilter();
                binding.appBarMain.txtTitle.setText(R.string.now_playing_movies);
                binding.appBarMain.content.layoutArrow.startAnimation(slideUp);
                parentSettingFragment.showData();
            }
        });

        binding.btnShowReminder.setOnClickListener(view -> {
            parentSettingFragment.goToNotification();
            binding.appBarMain.content.viewPager.setCurrentItem(2);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBarMain.txtTitle.setText("All Reminders");
        });
    }

    private void showProfile() {
        binding.txtName.setText(SharedPreferences.getName(MainActivity.this));
        binding.txtBirthday.setText(SharedPreferences.getBirthday(MainActivity.this));
        binding.txtMail.setText(SharedPreferences.getMail(MainActivity.this));
        binding.txtSex.setText(SharedPreferences.getSex(MainActivity.this));
        Uri uri = Uri.parse(SharedPreferences.getAvatar(this));
        Picasso.with(this).load(uri).transform(new CircleTransform()).error(R.drawable.bg_img_ava).into(binding.imgAva);
    }


    private void showNoti(){
        binding.layoutNoti.removeAllViews();
        List<Alarm> alarms = myDatabase.myDao().getAllAlarm();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        for (int i = 0; i<alarms.size(); i++){
            if(alarms.get(i).getTime()> Calendar.getInstance().getTimeInMillis()){
                ItemNotiBinding itemBinding = ItemNotiBinding.inflate(getLayoutInflater());
                itemBinding.time.setText(simpleDateFormat.format(new Date(alarms.get(i).getTime())));
                String title1 = alarms.get(i).getTitle1();
                String title2 = alarms.get(i).getTitle2();
                title2 = title2.replace("Year: ","-");
                title2 = title2.replace("Rate: ","-");
                itemBinding.title.setText(title1 + title2);
                binding.layoutNoti.addView(itemBinding.getRoot());
            }
            if(i == 1) break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        itemGrid = menu.findItem(R.id.showGrid);
        itemSearch = menu.findItem(R.id.search);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String newText) {
                favouriteFragment.search(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.movies:
                binding.appBarMain.content.viewPager.setCurrentItem(0);
                return true;
            case R.id.favourite:
                binding.appBarMain.content.viewPager.setCurrentItem(1);
                return true;
            case R.id.setting:
                binding.appBarMain.content.viewPager.setCurrentItem(2);
                return true;
            case R.id.about:
                binding.appBarMain.content.viewPager.setCurrentItem(3);
                return true;
            case R.id.showGrid:
                isGrid = !isGrid;
                if (isGrid) {
                    item.setIcon(R.mipmap.ic_list_white);
                } else {
                    item.setIcon(R.mipmap.ic_grid_on_white);
                }

                parentMoviesFragment.changeGrid(isGrid);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void changeTitleInMovies(){
        String title = getString(R.string.popular_movies);
        switch (SharedPreferences.getCategory(this)){
            case SharedPreferences.POPULAR:
                title = getString(R.string.popular_movies);
                break;
            case SharedPreferences.TOP_RATE:
                title = getString(R.string.top_rated_movies);
                break;
            case SharedPreferences.UPCOMING:
                title = getString(R.string.upcoming_movies);
                break;
            case SharedPreferences.NOW_PLAYING:
                title = getString(R.string.now_playing_movies);
                break;
        }
        binding.appBarMain.txtTitle.setText(title);
        binding.appBarMain.imgArrow.setVisibility(View.VISIBLE);
        binding.appBarMain.imgArrow.setImageResource(R.mipmap.ic_arrow_drop_down);
    }

    @Override
    public void onBackPressed() {
        if (binding.appBarMain.content.viewPager.getCurrentItem() == 0) {
            int count = parentMoviesFragment.getChildFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                parentMoviesFragment.getChildFragmentManager().popBackStack();
                changeTitleInMovies();
                binding.appBarMain.imgArrow.setVisibility(View.VISIBLE);
                if (itemGrid != null)
                    itemGrid.setVisible(true);
            }
        }else if(binding.appBarMain.content.viewPager.getCurrentItem() == 2){
            int count = parentSettingFragment.getChildFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                parentSettingFragment.getChildFragmentManager().popBackStack();
                binding.appBarMain.txtTitle.setText(R.string.action_settings);
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStarClick() {
        int count = 0;
        try {
            count = favouriteFragment.loadFavourite();
        } catch (NullPointerException e) {
            e.printStackTrace();
            count = myDatabase.myDao().getAll().size();
        }
        txtCount.setText(count + "");
    }

    @Override
    public void delete(int id) {
        int count = 0;
        count = favouriteFragment.loadFavourite();
        txtCount.setText(count + "");
        parentMoviesFragment.updateWhenDeleteFavourite(id);
    }

    @Override
    public void onClickStarInMovieDetailFragment(int id) {
        int count = 0;
        try {
            count = favouriteFragment.loadFavourite();
        } catch (NullPointerException e) {
            e.printStackTrace();
            count = myDatabase.myDao().getAll().size();
        }
        txtCount.setText(count + "");
        parentMoviesFragment.updateWhenDeleteFavourite(id);
    }

    @Override
    public void goTo(String title) {
        if (itemGrid != null)
            itemGrid.setVisible(false);
        binding.appBarMain.txtTitle.setText(title);
        binding.appBarMain.imgArrow.setVisibility(View.GONE);
        binding.appBarMain.content.layoutArrow.setVisibility(View.GONE);
    }

    @Override
    public void changeFilter() {
        parentMoviesFragment.moviesFragment.changeFilter();
    }

}