package com.example.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Introduction extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        CardView cardView_introduction = findViewById(R.id.CardView_introduction);
        cardView_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }

    //開啟登入頁面func
    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
