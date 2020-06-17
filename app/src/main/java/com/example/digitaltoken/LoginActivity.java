package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText emailEditText;
    EditText passwordEditText;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    emailEditText.setError("Password is Required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // login authentication

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "The User is Successfully Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }

    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
    }

    public void dontHave(View view) {
        Intent intent = new Intent(getApplicationContext(), TypeActivity.class);
        startActivity(intent);
    }






}
