package com.example.digitaltoken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

public class TokenStatus extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_status);

        toolbar = findViewById(R.id.myActionBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        String heading = intent.getStringExtra("iTitle");

        getSupportActionBar().setTitle(heading);

        String description = intent.getStringExtra("iDisc");

        getSupportActionBar().setSubtitle(description);


    }


}
