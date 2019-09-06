package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookInfoFragment extends Fragment {

    private static final String ISBN_TAG = "isbn";
    private static final String TITLE_TAG = "title";
    private static final String PRICE_TAG = "price";

    private String isbn;
    private String title;
    private int price;

    private TextView content_title,content_info;
    private int clickId;

    public static BookInfoFragment newInstance(String isbn,String title,int price) {
        BookInfoFragment book_info = new BookInfoFragment();
        Bundle args = new Bundle();
        args.putString(ISBN_TAG, isbn);
        args.putString(TITLE_TAG,title);
        args.putInt(PRICE_TAG,price);
        book_info.setArguments(args);
        return book_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.book_information, container, false);
        ImageView book_pic = view.findViewById(R.id.bi_book_pic);
        TextView book_name = view.findViewById(R.id.bi_book_name);
        TextView book_price = view.findViewById(R.id.bi_book_price);
        TextView book_author = view.findViewById(R.id.bi_book_author);
        TextView book_version = view.findViewById(R.id.bi_book_version);
        TextView book_publisher = view.findViewById(R.id.bi_book_publisher);
        TextView book_publishdate = view.findViewById(R.id.bi_book_publishdate);
        TextView book_content_info  = view.findViewById(R.id.bi_content_info);
        if (getArguments() != null) {
            isbn = getArguments().getString(ISBN_TAG);
            title = getArguments().getString(TITLE_TAG);
            price = getArguments().getInt(PRICE_TAG);
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()). getSupportActionBar().setTitle("返回");
        toolbar.bringToFront();

        //連資料庫
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/book_info/" + isbn);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListData bookInfo = dataSnapshot.getValue(ListData.class);
                Glide.with(BookInfoFragment.this)
                        .load(bookInfo.getUrl())
                        .into(book_pic);
                book_name.setText(bookInfo.getTitle());
                book_price.setText(bookInfo.getPrice()+" 元");
                book_author.setText(bookInfo.getAuthor());
                book_version.setText(bookInfo.getVersion());
                book_publisher.setText(bookInfo.getPublisher());
                book_publishdate.setText(bookInfo.getPublishDate());
                book_content_info.setText(bookInfo.getOutline());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });


        //尋找控件
        content_title = view.findViewById(R.id.bi_content_title);
        content_info = view.findViewById(R.id.bi_content_info);

        //設置延遲時間
        final LinearLayout transitionsContainer = view.findViewById(R.id.bi_container_down);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transitionsContainer);
        }
        //設置點擊事件
        BookInfoFragment.OnClick onClick = new BookInfoFragment.OnClick();
        content_title.setOnClickListener(onClick);

        return view;
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v){
            Intent intent = null;
            clickId = v.getId();
            switch (clickId){
                case R.id.bi_content_title:
                    toggleText(content_info,v);
                    //toggleContent(content_info);
                    //Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }

    public void toggleText(TextView textView,View view){
        int status = textView.getVisibility();
        if(status == 0){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
    }/*
    public void toggleContent(TextView textView){
        ObjectAnimator.ofFloat(textView,"alpha",0f,1f).setDuration(500).start();
    }*/
}
