package com.example.bookstore;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bookstore.fragments.MyProfileFragment;


public class MyProfileActivity extends AppCompatActivity {

    private static final String KEY_MY_PROFILE_FRAGMENT = "my-profile-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_button);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("返回");

    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        MyProfileFragment myprofileFragment = (MyProfileFragment) fragmentManager.findFragmentByTag(KEY_MY_PROFILE_FRAGMENT);

        if (myprofileFragment == null) {
            myprofileFragment = MyProfileFragment.newInstance();

            fragmentManager.beginTransaction()
                    .add(android.R.id.content, myprofileFragment, KEY_MY_PROFILE_FRAGMENT)
                    .commit();
        }
    }
    //返回上個fragment
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
