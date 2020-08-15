package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.os.Vibrator;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TokenStatus extends AppCompatActivity {

    private static final String CHANNEL_ID = "myChannel";
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
    String description = " ";
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
    boolean noCalculations = false;

    SharedPreferences alarmPreferences;
    SharedPreferences yourTokenPreferences;


    String alarmKey;
    String tokenKey;


    int savedAlarmToken;
    int savedYourToken;

    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Integer> times = new ArrayList<Integer>();
    CheckNetwork network = new CheckNetwork(this);
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
        description = intent.getStringExtra("location");
        userid = intent.getStringExtra("userId");
        getSupportActionBar().setTitle(heading);
        getSupportActionBar().setSubtitle(description);

        swipeRefreshLayout = findViewById(R.id.statusRefresh);
        network.myNetworkCheck();

        alarmPreferences = this.getSharedPreferences("com.example.digitaltoken", Context.MODE_PRIVATE);
        yourTokenPreferences = this.getSharedPreferences("com.example.digitaltoken", Context.MODE_PRIVATE);

        alarmKey = userid + "alarm";
        tokenKey = userid + "token";

        savedYourToken = yourTokenPreferences.getInt(tokenKey, 0);
        savedAlarmToken = alarmPreferences.getInt(alarmKey, 0);

        counterTV = findViewById(R.id.counterTV);
        timingTV = findViewById(R.id.timingTV);
        notificationTV = findViewById(R.id.notesTV);
        yourTokenTV = findViewById(R.id.myTokenTV);
        estimatedTv = findViewById(R.id.estimatedTV);
        oneTokenTV = findViewById(R.id.oneTokenTv);
        yourTokenTV.setText(String.valueOf(savedYourToken));

        audioPlayer = MediaPlayer.create(this, R.raw.alarm);

        start();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        userDatabaseReference.keepSynced(true);

        if (!userid.equals("A")) {
            userDatabaseReference.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getValue(Message.class).getmCount();
                    timing = dataSnapshot.getValue(Message.class).getmTime();
                    notifications = dataSnapshot.getValue(Message.class).getmNotificaton();
                    sAvgToken = dataSnapshot.getValue(Message.class).getAvgToken();

                    avgToken = Integer.parseInt(sAvgToken);
                    countIntDB = Integer.parseInt(count);
                    counterTV.setText(count);
                    timingTV.setText(timing);
                    notificationTV.setText(notifications);

                    Log.e("youe AVERAGE TOKEN IS", sAvgToken);
                    ringAlarm();
                    if (avgToken == -1) {
                        noCalculations = true;
                        oneTokenTV.setText(".....");
                        estimatedTv.setText(".....");
                    } else {
                        noCalculations = false;
                        start();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                network.myNetworkCheck();
                if (!CheckNetwork.isNetworkConnected) {
                    Toast.makeText(TokenStatus.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    userDatabaseReference.keepSynced(true);
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.setAlarm) {

            if (savedYourToken != 0) {
                alarmDialog();
                return true;
            } else {
                Toast.makeText(this, "Please enter Your Token before setting alarm", Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        return false;
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
                                savedYourToken = myIntToken;
                                yourTokenPreferences.edit().putInt(tokenKey, savedYourToken).apply();
                                yourTokenTV.setText(String.valueOf(savedYourToken));
                                start();   // for setting the estimated time.
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }

////////////////////////// time calculations

    public void start() {

        if (!noCalculations) {

            aseconds(avgToken);

            if (savedYourToken == countIntDB) {
                stopthree = true;
                savedYourToken = 0;
                savedAlarmToken = 0;
                yourTokenPreferences.edit().putInt(tokenKey, savedYourToken).apply();
                alarmPreferences.edit().putInt(alarmKey, savedAlarmToken).apply();

                yourTokenTV.setText(String.valueOf(savedYourToken));
                estimatedTv.setText(".....");

            }
            Log.e("the savedYourToken is ", Integer.toString(savedYourToken));

            if (savedYourToken > 7 && !noCalculations) {
                seconds(avgToken * (savedYourToken - countIntDB));
            } else {
                estimatedTv.setText(".....");
            }
        }

    }

    // Alarm Stuff
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
                            if (alarmIntToken >= (savedYourToken - countIntDB)) {
                                alarmEditText.setError("Invalid Enter ! Please enter lesser number");
                            } else {
                                savedAlarmToken = Integer.parseInt(alarmToken);
                                alarmPreferences.edit().putInt(alarmKey, savedAlarmToken).apply();
                                alarmBuilder.dismiss();
                            }
                        }
                    }
                });
            }
        });
        alarmBuilder.show();
    }

    public void dismissAlarm() {

        AlertDialog.Builder dismissDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_lock_idle_alarm)
                .setTitle("Dismiss the Alarm")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        audioPlayer.pause();
                        audioPlayer.seekTo(0);
                        //stopService();
                    }
                });
        try {
            dismissDialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("the window token is", e.toString());
        }
    }

    public void ringAlarm() {
        if (savedAlarmToken == 0) {

        } else if (savedYourToken == 0) {

        } else if (countIntDB == 0) {

        } else if ((savedYourToken - savedAlarmToken) == countIntDB) {
            //audioPlayer.start();
            //audioPlayer.setScreenOnWhilePlaying(true);
            savedAlarmToken = 0;
            alarmPreferences.edit().putInt(alarmKey, savedAlarmToken).apply();
            notificationPops();


        }
    }

    public void notificationPops() {

        Intent intent = new Intent(this, TokenStatus.class);

        intent.putExtra("userId", userid);
        intent.putExtra("name", heading);
        intent.putExtra("location", description);

        createNotificationChannel();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentTitle("Token Reminder")
                .setContentText("Dismiss the Reminder by Clicking this")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{100, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000})
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "myChannel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            serviceChannel.enableVibration(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
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
