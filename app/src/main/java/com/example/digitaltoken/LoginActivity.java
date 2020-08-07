package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText emailEditText;
    EditText passwordEditText;

    FirebaseAuth firebaseAuth;
    DatabaseReference usersDataReference;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;

    String inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        usersDataReference = FirebaseDatabase.getInstance().getReference("Users");


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

    public void forgotClick(View view) {

        LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflaters = getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.forgot_dialog, null);
        final EditText forgotEditText = dialogLayout.findViewById(R.id.forgotEmail);
        final AlertDialog forgotBuilder = new AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setPositiveButton("Send", null)
                .setNegativeButton("cancel", null)
                .create();
        forgotBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) forgotBuilder).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        inputEmail = forgotEditText.getText().toString();

                        if (TextUtils.isEmpty(inputEmail)) {
                            forgotEditText.setError("Please Enter Your Email");
                        } else {

                            firebaseAuth.sendPasswordResetEmail(inputEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this,
                                                        "The Reset link has send to your email provided",
                                                        Toast.LENGTH_SHORT).show();
                                                forgotBuilder.dismiss();
                                            } else {
                                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }
                    }
                });
            }
        });
        forgotBuilder.show();

    }


}
