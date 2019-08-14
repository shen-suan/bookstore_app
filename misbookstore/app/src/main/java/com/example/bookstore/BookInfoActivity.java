package com.example.bookstore;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bookstore.fragments.BookInfoFragment;
import com.example.bookstore.fragments.MyProfileFragment;

public class BookInfoActivity extends AppCompatActivity {

    private static final String KEY_BOOK_INFO_FRAGMENT = "book-info-fragment";
    private String isbn;
    private String title;
    private int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        isbn = bundle.getString("isbn");
        title = bundle.getString("title");
        price = bundle.getInt("price");

    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        BookInfoFragment bookinfoFragment = (BookInfoFragment) fragmentManager.findFragmentByTag(KEY_BOOK_INFO_FRAGMENT);
        if (bookinfoFragment == null) {
            bookinfoFragment = BookInfoFragment.newInstance(isbn,title,price);

            fragmentManager.beginTransaction()
                    .add(android.R.id.content, bookinfoFragment, KEY_BOOK_INFO_FRAGMENT)
                    .commit();
        }
    }
}
