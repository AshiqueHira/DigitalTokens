package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class TokenStatus extends AppCompatActivity {

    Toolbar toolbar;
    TextView timingTV;
    TextView notificationTV;
    TextView counterTV;
    TextView yourTokenTV;

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

                    countInt = Integer.parseInt(count);
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
        AlertDialog.Builder alarmBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflaters = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.alarm_layout, null);
        final EditText alarmEditText = dialogLayout.findViewById(R.id.tokensEditText);
        alarmBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmToken = alarmEditText.getText().toString();
                alarmIntToken = Integer.parseInt(alarmToken);
                //Toast.makeText(TokenStatus.this, alarmIntToken, Toast.LENGTH_SHORT).show();

            }
        });
        alarmBuilder.setNegativeButton("cancel", null);
        alarmBuilder.setView(dialogLayout);
        alarmBuilder.show();
    }

    public void yourTokenClick(View view) {

        AlertDialog.Builder tokenBuilder = new AlertDialog.Builder(this);
        tokenBuilder.setTitle("Enter your Token Number");

        final EditText input = new EditText(TokenStatus.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        tokenBuilder.setView(input);
        tokenBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myToken = input.getText().toString();
                myIntToken = Integer.parseInt(myToken);
                if (myIntToken <= countInt) {
                    input.setError("Your token must be greater than the current token number");
                    return;
                }
                //Toast.makeText(getApplicationContext(), "Text entered is " + input.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        tokenBuilder.setNegativeButton("Cancel", null);
        tokenBuilder.show();

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



}
