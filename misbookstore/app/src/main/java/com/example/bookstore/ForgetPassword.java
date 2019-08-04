package com.example.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class ForgetPassword extends AppCompatActivity {

    private CardView CardView_FPassword;
    private TextView textView3_FPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);

        CardView_FPassword = (CardView) findViewById(R.id.CardView_FPassword);
        CardView_FPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });


        textView3_FPassword = (TextView) findViewById(R.id.textView3_FPassword);
        textView3_FPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

    }

    //開啟登入頁面func
    public void openMainActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
