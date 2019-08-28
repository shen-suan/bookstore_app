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
        Bundle bundle = getIntent().getExtras();
        isbn = bundle.getString("isbn");
        title = bundle.getString("title");
        price = bundle.getInt("price");

        Toolbar toolbar_info = findViewById(R.id.toolbar_bookinfo);
        setSupportActionBar(toolbar_info);

        searchView_info = findViewById(R.id.searchView_bookinfo);

    }

    //跑不進去此fuc
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.onback_menu, menu);
        System.out.println("1111111111111111111111111111");
        setupSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_example_info:
                Intent intent = new Intent(this, Voice_Assistant.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search_info);
        searchView_info.setMenuItem(item);


        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView_info.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, this);
    }

//    @Override
//    public void onBackPressed() {
//        if (searchView_info.onBackPressed()) {
//            return;
//        }
//
//        super.onBackPressed();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (searchView_info.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
