package com.example.bookstore;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;

import com.example.bookstore.fragments.MyProfileFragment;


public class MyProfileActivity extends AppCompatActivity {

    private static final String KEY_MY_PROFILE_FRAGMENT = "my-profile-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

}
