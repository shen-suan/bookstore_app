package com.example.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    protected Button textView2_SignupBtn;
    protected TextView textView_signup_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        textView2_SignupBtn = findViewById(R.id.textView2_SignupBtn);
        textView2_SignupBtn.setOnClickListener(new View.OnClickListener() {
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
