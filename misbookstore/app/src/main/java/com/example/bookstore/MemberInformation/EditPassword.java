package com.example.bookstore.MemberInformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookstore.R;

public class EditPassword extends AppCompatActivity {
    private Button btn_current_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_password);

        btn_current_password = (Button) findViewById(R.id.btn_current_password);
        btn_current_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(EditPassword.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
