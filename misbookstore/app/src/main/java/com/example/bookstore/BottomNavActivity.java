package com.example.bookstore;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.ViewPager;
import android.databinding.DataBindingUtil;

import android.support.v4.app.Fragment;

import com.example.bookstore.arContent.augmentedimage.AugmentedImageActivity;
import com.example.bookstore.fragments.BookFragment;
import com.example.bookstore.fragments.FavoriteFragment;
import com.example.bookstore.fragments.HomepageFragment;
import com.example.bookstore.fragments.ProfileFragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.annotation.NonNull;


import com.example.bookstore.databinding.ActivityBottomNavBinding;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;

import java.util.ArrayList;
import java.util.List;

public class BottomNavActivity extends AppCompatActivity {

    private static final String TAG = BottomNavActivity.class.getSimpleName();
    private ActivityBottomNavBinding bind;
    private VpAdapter adapter;
    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    private SimpleSearchView searchView;

    // collections
    private List<Fragment> fragments;// used for ViewPager adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_with_view_pager);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav);

        initData();
        initView();
        initEvent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.searchView);
        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SimpleSearchView", "Text changed:" + newText);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        setupSearchView(menu);
        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

//        searchView.onActionViewCollapsed();
//        searchView.setQuery("", false);
//        searchView.clearFocus();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_example:
                Intent intent = new Intent(this, Voice_Assistant.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                return super.onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, this);
    }



    /**
     * create fragments
     */
    private void initData() {
        fragments = new ArrayList<>(4);

        // create fragment and add it
        Fragment musicFragment = new HomepageFragment();
        Fragment backupFragment = new BookFragment();
        Fragment favorFragment = new FavoriteFragment();
        Fragment visibilityFragment = new ProfileFragment();



        // add to fragments for adapter
        fragments.add(musicFragment);
        fragments.add(backupFragment);
        fragments.add(favorFragment);
        fragments.add(visibilityFragment);
    }


    /**
     * change BottomNavigationViewEx style
     */
    private void initView() {
        bind.bnve.enableItemShiftingMode(false);
        bind.bnve.enableShiftingMode(true);
        bind.bnve.enableAnimation(false);
//        int item=5;
//        for(int i = 0; i < item; i++) {
//            bind.bnve.setItemBackground(i, R.color.color_nav_back);
//        }
        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        bind.vp.setAdapter(adapter);
    }

    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item
        bind.bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        position = 0;
                        break;
                    case R.id.navigation_book:
                        position = 1;
                        break;
                    case R.id.navigation_favorites:
                        position = 2;
                        break;
                    case R.id.navigation_profile:
                        position = 3;
                        break;
                    case R.id.i_empty: {
                        return false;
                    }
                }
                if(previousPosition != position) {
                    bind.vp.setCurrentItem(position, false);
                    previousPosition = position;
                    Log.i(TAG, "-----bnve-------- previous item:" + bind.bnve.getCurrentItem() + " current item:" + position + " ------------------");
                }

                return true;
            }
        });

        // set listener to change the current checked item of bottom nav when scroll view pager 左右滑動屏幕切換
        bind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "-----ViewPager-------- previous item:" + bind.bnve.getCurrentItem() + " current item:" + position + " ------------------");
                if (position >= 2)// 2 is center
                    position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                bind.bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // center item click listener  掃描
        bind.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pass = new Intent();
                pass.setClass(BottomNavActivity.this, AugmentedImageActivity.class);
                startActivity(pass);
            }
        });
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
