package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookstore.BookInformation.BookInformation;
import com.example.bookstore.R;

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

    private TextView content_title,content_info,author_title,author_info;
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
        TextView book_name = view.findViewById(R.id.bi_book_name);
        TextView book_price = view.findViewById(R.id.bi_book_price);
        if (getArguments() != null) {
            isbn = getArguments().getString(ISBN_TAG);
            title = getArguments().getString(TITLE_TAG);
            price = getArguments().getInt(PRICE_TAG);
        }
        book_name.setText(title);
        book_price.setText(price+" 元");

        //尋找控件
        content_title = view.findViewById(R.id.bi_content_title);
        content_info = view.findViewById(R.id.bi_content_info);
        author_title = view.findViewById(R.id.bi_author_title);
        author_info = view.findViewById(R.id.bi_author_info);
        //設置延遲時間
        final LinearLayout transitionsContainer = view.findViewById(R.id.bi_container_down);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transitionsContainer);
        }
        //設置點擊事件
        BookInfoFragment.OnClick onClick = new BookInfoFragment.OnClick();
        content_title.setOnClickListener(onClick);
        author_title.setOnClickListener(onClick);

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
                case R.id.bi_author_title:
                    toggleText(author_info,v);
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
