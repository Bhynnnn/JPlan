package com.example.jplan.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.jplan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login_btn, register_btn, findID_btn, findPW_btn;
    String email_str, pw_str;

    private TextInputEditText log_email_edt, log_pw_edt;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.login_btn);
        log_email_edt = findViewById(R.id.log_email_edt);
        log_pw_edt = findViewById(R.id.log_pw_edt);
        register_btn = findViewById(R.id.register_btn);
        findID_btn = findViewById(R.id.findID_btn);
        findPW_btn = findViewById(R.id.findPW_btn);

        findViewById(R.id.login_btn).bringToFront();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_str = log_email_edt.getText().toString();
                pw_str = log_pw_edt.getText().toString();
                if ((TextUtils.isEmpty(email_str)) || (TextUtils.isEmpty(pw_str))) {
                    System.out.println("login fail");
                } else {
                    auth.signInWithEmailAndPassword(email_str, pw_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                System.out.println("login success");
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });
                }
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

    }
}