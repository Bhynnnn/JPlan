package com.example.jplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    Button add_account_btn;
    EditText email_edt, name_edt, pw_edt, pwA_edt;
    String email, name, pw, pwAgain;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        add_account_btn = findViewById(R.id.add_account_btn);
        email_edt = findViewById(R.id.email_edt);
        name_edt = findViewById(R.id.name_edt);
        pw_edt = findViewById(R.id.pw_edt);
        pwA_edt = findViewById(R.id.pwA_edt);


        add_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_edt.getText().toString();
                name = name_edt.getText().toString();
                pw = pw_edt.getText().toString();
                pwAgain = pwA_edt.getText().toString();

                if (CheckEmpty(email, name, pw, pwAgain) == true) {
                    if (pw.equals(pwAgain)) {
                        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User userAccount = new User();
                                    userAccount.setEmailId(email);
                                    userAccount.setName(name);
                                    userAccount.setPassword(pw);
                                    userAccount.setUid(auth.getCurrentUser().getUid());

                                    fireStore.collection("User")
                                            .document(auth.getCurrentUser().getUid())
                                            .set(userAccount)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    System.out.println("회원가입 성공");
                                                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                                                    finish();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("회원가입 실패");
                                                }
                                            });
                                }
                            }
                        });
                    }
                }

                Intent createAccount = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(createAccount);
                finish();
            }
        });
    }

    private boolean CheckEmpty(String... params) {
        for (String param : params) {
            if (param == null || param.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}