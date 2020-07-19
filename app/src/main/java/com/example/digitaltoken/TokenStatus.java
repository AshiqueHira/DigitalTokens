package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TokenStatus extends AppCompatActivity {

    Toolbar toolbar;
    TextView timingTV;
    TextView notificationTV;
    TextView counterTV;
    TextView yourTokenTV;
    TextView oneTokenTV;
    TextView estimatedTv;

    // String initialisation
    String count = "0";
    String timing = "OPEN : 8:00 AM and CLOSE : 5:00 PM";
    String notifications = "No Notificatons yet";
    String userid = "GOMe2uXrxHWnoztbrm243SZYpXM2";
    String alarmToken = "0";
    int alarmIntToken = 0;
    String heading = " ";
    String myToken = "0";
    String sAvgToken = "0";

    int avgToken = 0;
    int myIntToken = 0;
    int countIntDB = 0;

    DatabaseReference userDatabaseReference;
    MediaPlayer audioPlayer;



    private boolean isRunning = false;
    boolean callingTwice = false;
    boolean referenceNumBol = false;

    boolean stopthree = false;


    SharedPreferences alarmPreferences;
    SharedPreferences yourTokenPreferences;

    int savedAlarmToken;
    int savedYourToken;


    ArrayList<Integer> times = new ArrayList<Integer>();
    String sDuration;
    int iDuration;
    int rcounter;
    int sum = 0;
    int refenceCount = 1;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.token_status_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_status);

        toolbar = findViewById(R.id.myActionBar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        heading = intent.getStringExtra("name");
        getSupportActionBar().setTitle(heading);
        String description = intent.getStringExtra("location");
        getSupportActionBar().setSubtitle(description);
        userid = intent.getStringExtra("userId");

        alarmPreferences = this.getSharedPreferences("com.example.digitaltoken", Context.MODE_PRIVATE);
        yourTokenPreferences = this.getSharedPreferences("com.example.digitaltoken", Context.MODE_PRIVATE);

        savedYourToken = yourTokenPreferences.getInt("mToken", 0);
        savedAlarmToken = alarmPreferences.getInt("mAlarm", 0);

        counterTV = findViewById(R.id.counterTV);
        timingTV = findViewById(R.id.timingTV);
        notificationTV = findViewById(R.id.notesTV);
        yourTokenTV = findViewById(R.id.myTokenTV);
        estimatedTv = findViewById(R.id.estimatedTV);
        oneTokenTV = findViewById(R.id.oneTokenTv);

        audioPlayer = MediaPlayer.create(this, R.raw.alarm);


        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Messages");

        if (!userid.equals("A")) {
            userDatabaseReference.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getValue(Message.class).getmCount();
                    timing = dataSnapshot.getValue(Message.class).getmTime();
                    notifications = dataSnapshot.getValue(Message.class).getmNotificaton();
                    sAvgToken = dataSnapshot.getValue(Message.class).getAvgToken();

                    avgToken = Integer.parseInt(sAvgToken);
                    start();

                    countIntDB = Integer.parseInt(count);
                    counterTV.setText(count);
                    timingTV.setText(timing);
                    notificationTV.setText(notifications);
                    ringAlarm();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    public void alarmDialog() {

        LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflaters = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.alarm_layout, null);
        final EditText alarmEditText = dialogLayout.findViewById(R.id.tokensEditText);
        final AlertDialog alarmBuilder = new AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setPositiveButton("Set", null)
                .setNegativeButton("cancel", null)
                .create();
        alarmBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) alarmBuilder).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        alarmToken = alarmEditText.getText().toString();

                        if (TextUtils.isEmpty(alarmToken)) {
                            alarmEditText.setError("Please enter a number");
                        } else {
                            alarmIntToken = Integer.parseInt(alarmToken);
                            if (alarmIntToken >= (myIntToken - countIntDB)) {
                                alarmEditText.setError("Invalid Enter ! Please enter lesser number");
                            } else {
                                alarmIntToken = Integer.parseInt(alarmToken);
                                alarmPreferences.edit().putInt("mAlarm", alarmIntToken).apply();
                                alarmBuilder.dismiss();
                            }
                        }
                    }
                });
            }
        });
        alarmBuilder.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.setAlarm) {

            alarmDialog();
            return true;
        }
        return false;
    }

    public void ringAlarm() {
        if (savedAlarmToken == 0) {

        } else if (savedYourToken == 0) {

        } else if (countIntDB == 0) {

        } else if ((savedYourToken - savedAlarmToken) == countIntDB) {
            audioPlayer.start();
            dismissAlarm();
        }

    }

    public void yourTokenClick(View view) {

        final EditText input = new EditText(TokenStatus.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("Enter your Token Number")
                .setPositiveButton("Set", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        myToken = input.getText().toString();
                        if (TextUtils.isEmpty(myToken)) {
                            input.setError("Please Enter your Token Number");
                        } else {
                            myIntToken = Integer.parseInt(myToken);
                            if (myIntToken <= countIntDB) {
                                input.setError("Your token must be greater than the current token number");
                            } else {
                                myIntToken = Integer.parseInt(myToken);
                                yourTokenPreferences.edit().putInt("mToken", myIntToken).apply();
                                yourTokenTV.setText(myToken);
                                start();
                                dialog.dismiss();
                            }
                        }

                    }
                });
            }
        });
        dialog.show();
    }

    public void dismissAlarm() {

        AlertDialog dismissDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_lock_idle_alarm)
                .setTitle("Dismiss the Alarm")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        audioPlayer.stop();
                    }
                })
                .show();

    }


////////////////////////// time calculations

    public void start() {

        aseconds(avgToken);
        if (savedYourToken == 0) {
            stopthree = true;
        }

        if (savedYourToken == countIntDB) {
            stopthree = true;
            savedYourToken = 0;
            savedAlarmToken = 0;
            yourTokenPreferences.edit().putInt("mToken", savedYourToken).apply();
            alarmPreferences.edit().putInt("mAlarm", savedAlarmToken).apply();

        }
        Log.e("the error is ", Integer.toString(savedYourToken));
        if (stopthree != true) {
            if (savedYourToken > 7) {
                seconds(avgToken * (savedYourToken - countIntDB));
            } else {
                estimatedTv.setText("...");
            }
        }



    }

    // these three are for estimated time calculations that is seconds
    @SuppressLint("SetTextI18n")
    public void seconds(int second) {
        int checkSecond = second / 60;
        if (checkSecond == 0) {
            estimatedTv.setText(String.valueOf(second) + " sec");
        } else {
            minutes(second);
        }

    }

    @SuppressLint("SetTextI18n")
    public void minutes(int passedSec) {
        int minute = passedSec / 60;
        if (minute >= 60) {
            hours(minute);
        } else {
            estimatedTv.setText(String.valueOf(minute) + " min");
        }

    }

    @SuppressLint("SetTextI18n")
    public void hours(int passedMin) {
        int hour = passedMin / 60;
        int remMin = passedMin % 60;
        String sHour = String.valueOf(hour);
        String sRemMin = String.valueOf(remMin);
        estimatedTv.setText(sHour + " hr " + sRemMin + " min");

    }

    // these three are for average time calculations that is aseconds
    @SuppressLint("SetTextI18n")
    public void aseconds(int asecond) {
        int acheckSecond = asecond / 60;
        if (acheckSecond == 0) {
            oneTokenTV.setText(String.valueOf(asecond) + " sec");
        } else {
            aminutes(asecond);
        }

    }

    @SuppressLint("SetTextI18n")
    public void aminutes(int apassedSec) {
        int aminute = apassedSec / 60;
        if (aminute >= 60) {
            ahours(aminute);
        } else {
            oneTokenTV.setText(String.valueOf(aminute) + " min");
        }

    }

    @SuppressLint("SetTextI18n")
    public void ahours(int apassedMin) {
        int ahour = apassedMin / 60;
        int aremMin = apassedMin % 60;
        String asHour = String.valueOf(ahour);
        String asRemMin = String.valueOf(aremMin);
        oneTokenTV.setText(asHour + " hr " + asRemMin + " min");

    }

}
