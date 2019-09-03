package com.example.bookstore;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.bookstore.fragments.BookInfoFragment;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;

public class BookInfoActivity extends AppCompatActivity {

    private static final String KEY_BOOK_INFO_FRAGMENT = "book-info-fragment";
    private String isbn;
    private String title;
    private int price;

    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    public SimpleSearchView searchView_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_button);
        Bundle bundle = getIntent().getExtras();
        isbn = bundle.getString("isbn");
        title = bundle.getString("title");
        price = bundle.getInt("price");

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
        BookInfoFragment bookinfoFragment = (BookInfoFragment) fragmentManager.findFragmentByTag(KEY_BOOK_INFO_FRAGMENT);
        if (bookinfoFragment == null) {
            bookinfoFragment = BookInfoFragment.newInstance(isbn,title,price);

            fragmentManager.beginTransaction()
                    .add(android.R.id.content, bookinfoFragment, KEY_BOOK_INFO_FRAGMENT)
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
