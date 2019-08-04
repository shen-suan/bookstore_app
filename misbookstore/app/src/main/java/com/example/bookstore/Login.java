package com.example.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private CardView CardView_login;
    private TextView textView_login_signup;
    private TextView textView_login_FPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        CardView_login = (CardView) findViewById(R.id.CardView_login);
        CardView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });



//        程式執行會當掉
        textView_login_signup = (TextView) findViewById(R.id.textView_login_signup);
        textView_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });


        textView_login_FPassword = (TextView) findViewById(R.id.textView_login_FPassword);
        textView_login_FPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetPassword();
            }
        });



    }



   //開啟首頁頁面func
    public void openHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


//開啟註冊頁面func
    public void openSignup(){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

//忘記密碼頁面func
    public void openForgetPassword(){
        Intent intent = new Intent(this, ForgetPassword.class);
        startActivity(intent);
    }

}
