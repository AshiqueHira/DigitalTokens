package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.SystemClock;
import android.text.InputType;
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
    int myIntToken = 0;
    int countInt = 0;

    DatabaseReference userDatabaseReference;
    MediaPlayer audioPlayer;


    private Chronometer chronometer;
    private boolean isRunning = false;
    boolean callingTwice = false;

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


        counterTV = findViewById(R.id.counterTV);
        timingTV = findViewById(R.id.timingTV);
        notificationTV = findViewById(R.id.notesTV);
        yourTokenTV = findViewById(R.id.myTokenTV);

        estimatedTv = findViewById(R.id.estimatedTV);
        oneTokenTV = findViewById(R.id.oneTokenTv);

        audioPlayer = MediaPlayer.create(this, R.raw.alarm);
        chronometer = new Chronometer(this);

        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Messages");

        if (!userid.equals("A")) {
            userDatabaseReference.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getValue(Message.class).getmCount();
                    timing = dataSnapshot.getValue(Message.class).getmTime();
                    notifications = dataSnapshot.getValue(Message.class).getmNotificaton();

                    countInt = Integer.parseInt(count);
                    counterTV.setText(count);
                    timingTV.setText(timing);
                    notificationTV.setText(notifications);
                    ringAlarm();

                    if (countInt == refenceCount && !callingTwice) {
                        start();
                        refenceCount += 1;
                        callingTwice = true;
                    } else if (countInt == refenceCount && callingTwice) {
                        start();
                        refenceCount += 1;
                        callingTwice = true;
                        start();
                    }



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
                        // TODO Do something
                        alarmToken = alarmEditText.getText().toString();
                        alarmIntToken = Integer.parseInt(alarmToken);
                        if (myIntToken - countInt >= alarmIntToken) {
                            alarmEditText.setError("Invalid Enter ! Please enter lesser number");
                        } else {
                            alarmBuilder.dismiss();
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
        if (alarmIntToken == 0) {

        } else if (myIntToken == 0) {

        } else if (countInt == 0) {

        } else if (myIntToken - alarmIntToken == countInt) {
            audioPlayer.start();
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
                        myIntToken = Integer.parseInt(myToken);
                        if (myIntToken <= countInt) {
                            input.setError("Your token must be greater than the current token number");

                        } else {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }


////////////////////////// time calculations

    public void start() {

        if (!isRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isRunning = true;

        } else {

            isRunning = false;
            chronometer.stop();
            int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
            iDuration = elapsedMillis / 1000;
            sDuration = String.valueOf(iDuration);
            times.add(iDuration);
            rcounter += 1;

        }
        if (rcounter > 6) {
            int j = rcounter - 7;
            sum = 0;
            int avg = 0;
            for (int i = rcounter - 1; (i + 1) > j; i--) {
                sum += times.get(i);
                Log.e("the sum is ", String.valueOf(sum));
            }

            seconds(sum);
            avg = sum / 7;
            oneTokenTV.setText(String.valueOf(avg));

            Log.e("the counter is", String.valueOf(rcounter));
        }

    }

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

    public void hours(int passedMin) {
        int hour = passedMin / 60;
        int remMin = passedMin % 60;
        String sHour = String.valueOf(hour);
        String sRemMin = String.valueOf(remMin);
        estimatedTv.setText(sHour + " hr " + sRemMin + " min");

    }






}
