package com.example.digitaltoken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    Button registerButton;
    TextView alreadyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerButton = findViewById(R.id.registerButton);
        alreadyTextView = findViewById(R.id.alreadyTextView);

        Intent intent = getIntent();
    }

    public void register(View view) {

        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
    }

    public void already(View view) {

        finish();
    }
}
