package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView notesTextView;
    TextView counterTextView;
    TextView openTextView;

    Button counterEditButton;
    Button notesEditButton;

    AlertDialog dialogCounter;
    AlertDialog dialogNotes;
    AlertDialog dialogOpen;

    EditText counterEditText;
    EditText notesEditText;
    EditText openEditText;

    int count = 0;
    String value;

    DatabaseReference msgDataReference;
    FirebaseUser msg;

    String counter = Integer.toString(count);
    String timings = "...";
    String notifications = "No Notifications Yet";
    String userid = "b";
    String avgToken = "0";

    boolean startingCall = true;
    // Average time calculation stuff

    private Chronometer chronometer;
    private boolean isRunning = false;
    ArrayList<Integer> times = new ArrayList<Integer>();
    SharedPreferences arrayPreferences;
    int intDuration;
    int sevenSetter = 0;
    int avg = 0;
    int dbControlInt;
    boolean dbControlBol = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.myActionBar);
        setSupportActionBar(toolbar);

        notesTextView = findViewById(R.id.notesTV);
        counterTextView = findViewById(R.id.counterTV);
        openTextView = findViewById(R.id.timingTV);

        timings = openTextView.getText().toString();

        // Average time calculations stuff
        chronometer = new Chronometer(this);

        msg = FirebaseAuth.getInstance().getCurrentUser();
        userid = msg.getUid();
        msgDataReference = FirebaseDatabase.getInstance().getReference("Messages");

        /*msgDataReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(Message.class).getmCount();
                count = Integer.parseInt(value);
                counterTextView.setText(value);

                timings = dataSnapshot.getValue(Message.class).getmTime();
                openTextView.setText(timings);

                notifications = dataSnapshot.getValue(Message.class).getmNotificaton();
                notesTextView.setText(notifications);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        // calling the DB for only reading the average value
        msgDataReference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(Message.class).getmCount();
                timings = dataSnapshot.getValue(Message.class).getmTime();
                notifications = dataSnapshot.getValue(Message.class).getmNotificaton();
                count = Integer.parseInt(value);

                if (startingCall) {
                    counterTextView.setText(value);
                    openTextView.setText(timings);
                    notesTextView.setText(notifications);
                    startingCall = false;
                }
                if (!dbControlBol) {
                    dbControlInt = count;
                    dbControlBol = true;
                }
                if (dbControlInt != count) {
                    dbControlInt += 1;
                    chronoMethod();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //count = Integer.parseInt((String) counterTextView.getText());
        counterEditButton = findViewById(R.id.counterEditbutton);
        notesEditButton = findViewById(R.id.notesEditbutton);

        counterEditText = new EditText(this);
        counterEditText.setHint("Enter the Count value here");
        counterEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        notesEditText = new EditText(this);
        notesEditText.setHint("Enter the Notes here");

        openEditText = new EditText(this);
        openEditText.setHint("OPEN : 8:00 AM & CLOSE : 5:00 PM");
        openEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});


        dialogCounter = new AlertDialog.Builder(this).create();
        dialogNotes = new AlertDialog.Builder(this).create();
        dialogOpen = new AlertDialog.Builder(this).create();


        dialogCounter.setTitle(" Enter the Value ");
        dialogCounter.setView(counterEditText);

        dialogNotes.setTitle(" Enter the Notes");
        dialogNotes.setView(notesEditText);

        dialogOpen.setTitle("Enter the Time");
        dialogOpen.setView(openEditText);

        dialogCounter.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                counterTextView.setText(counterEditText.getText());
                value = counterEditText.getText().toString();
                count = Integer.parseInt(value);

                // value to be passed to firebase
                counter = value;
                updateDatas();
            }
        });


        dialogNotes.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifications = notesEditText.getText().toString();
                notesTextView.setText(notifications);

                // value to be passed to firebase

                updateDatas();
            }

        });

        dialogOpen.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                timings = openEditText.getText().toString();
                openTextView.setText(timings);

                // value to be passed to firebase
                updateDatas();
            }
        });

        openTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openEditText.setText(openTextView.getText().toString());
                dialogOpen.show();
                return true;
            }
        });


    }

    public void counterClick(View view) {

        counterEditText.setText(counterTextView.getText());
        dialogCounter.show();
    }

    public void notesClick(View view) {
        notesEditText.setText(notesTextView.getText());
        dialogNotes.show();
    }

    public void plusOne(View view) {

        count += 1;
        counterTextView.setText(Integer.toString(count));

        /// value to be passed to firebase
        counter = Integer.toString(count);
        updateDatas();

    }

    public void minusOne(View view) {

        count -= 1;
        counterTextView.setText(Integer.toString(count));

        /// value to be passed to firebase
        counter = Integer.toString(count);
        updateDatas();
    }

    // update datas into database.........................
    public void updateDatas() {
        msgDataReference.child(userid).child("mCount").setValue(counter);
        msgDataReference.child(userid).child("mNotificaton").setValue(notifications);
        msgDataReference.child(userid).child("mTime").setValue(timings);
        msgDataReference.child(userid).child("avgToken").setValue(avgToken);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.signout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.profile) {
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);
        }
        return false;
    }


    public void chronoMethod() {

        if (!isRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isRunning = true;

        } else {
            chronometer.stop();
            int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
            intDuration = elapsedMillis / 1000;
            times.add(intDuration);
            sevenSetter += 1;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
        if (sevenSetter > 6) {
            int j = sevenSetter - 7;
            int sum = 0;
            avg = 0;
            for (int i = sevenSetter - 1; (i + 1) > j; i--) {
                sum += times.get(i);
                Log.e("the sum is ", String.valueOf(sum));
            }
            avg = sum / 7;
            avgToken = String.valueOf(avg);
            Log.e("the counter is", String.valueOf(sevenSetter));
        }
    }
}

