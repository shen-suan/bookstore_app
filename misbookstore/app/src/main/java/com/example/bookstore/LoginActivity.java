package com.example.bookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設定 FirebaseAuth 介面
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        System.out.println(user);
        if(user == null){
            setContentView(R.layout.login);
            EditText accountEdit = findViewById(R.id.account_edit);
            EditText passwordEdit = findViewById(R.id.password_edit);
            TextInputLayout accoutLayout =  findViewById(R.id.account_layout);
            TextInputLayout passwordLayout = findViewById(R.id.password_layout);
            passwordLayout.setErrorEnabled(true);
            accoutLayout.setErrorEnabled(true);
            Button loginBtn = findViewById(R.id.textView_loginBtn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();
                    if(TextUtils.isEmpty(account)){
                        accoutLayout.setError(getString(R.string.plz_input_accout));
                        return;
                    }
                    if(TextUtils.isEmpty(password)){
                        passwordLayout.setError(getString(R.string.plz_input_pw));
                        return;
                    }
                    accoutLayout.setError("");
                    passwordLayout.setError("");
                    mAuth.signInWithEmailAndPassword(account, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, BottomNavActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            TextView signup = findViewById(R.id.textView_login_signup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
        } else{
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, BottomNavActivity.class);
            startActivity(intent);
            finish();
        }//end else

        //忘記密碼
//        TextView textView_login_FPassword = findViewById(R.id.textView_login_FPassword);
//        textView_login_FPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openForgetPassword();
//            }
//        });
    }

//忘記密碼頁面func
    public void openForgetPassword(){
        Intent intent = new Intent(this, ForgetPassword.class);
        startActivity(intent);
    }

}
