package com.example.digitaltoken;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
