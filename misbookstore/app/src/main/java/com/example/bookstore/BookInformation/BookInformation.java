package com.example.bookstore.BookInformation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;

//import com.istore.member.MemberInformation.TextDialog;

public class BookInformation extends AppCompatActivity {
    private TextView content_title,content_info,author_title,author_info;
    private int clickId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.book_information);
//
        //尋找控件
        content_title = findViewById(R.id.bi_content_title);
        content_info = findViewById(R.id.bi_content_info);
        author_title = findViewById(R.id.bi_author_title);
        author_info = findViewById(R.id.bi_author_info);
        //設置延遲時間
        final LinearLayout transitionsContainer = findViewById(R.id.bi_container_down);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transitionsContainer);
        }
        //設置點擊事件
        OnClick onClick = new OnClick();
        content_title.setOnClickListener(onClick);
        author_title.setOnClickListener(onClick);

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
            textView.setVisibility(view.GONE);
        }else {
            textView.setVisibility(view.VISIBLE);
        }
    }/*
    public void toggleContent(TextView textView){
        ObjectAnimator.ofFloat(textView,"alpha",0f,1f).setDuration(500).start();
    }*/
}
