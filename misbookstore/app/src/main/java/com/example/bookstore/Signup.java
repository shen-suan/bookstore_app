package com.example.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    protected CardView CardView_Signup;
    protected TextView textView_signup_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        CardView_Signup = findViewById(R.id.CardView_Signup);
        CardView_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        textView_signup_login = findViewById(R.id.textView_signup_login);
        textView_signup_login.setOnClickListener(new View.OnClickListener() {
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
