package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    String timings = "b";
    String notifications = "b";
    String userid = "b";

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

        notesTextView = findViewById(R.id.notesTextView);
        counterTextView = findViewById(R.id.counterTextView);
        openTextView = findViewById(R.id.openTextView);

        timings = openTextView.getText().toString();

        msg = FirebaseAuth.getInstance().getCurrentUser();
        userid = msg.getUid();
        msgDataReference = FirebaseDatabase.getInstance().getReference("Users");




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
                saveDatas();
            }
        });


        dialogNotes.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesTextView.setText(notesEditText.getText());

                // value to be passed to firebase
                notifications = notesEditText.getText().toString();
                saveDatas();
            }

        });

        dialogOpen.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openTextView.setText(openEditText.getText());

                // value to be passed to firebase
                timings = openEditText.getText().toString();
                saveDatas();
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


        msgDataReference = FirebaseDatabase.getInstance().getReference("Users");
        msgDataReference.child(userid).child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        saveDatas();

    }

    public void minusOne(View view) {

        count -= 1;
        counterTextView.setText(Integer.toString(count));

        /// value to be passed to firebase
        counter = Integer.toString(count);
        saveDatas();
    }


    public void saveDatas() {

        msgDataReference.child(userid).child(userid).child("counter").setValue(counter);
        msgDataReference.child(userid).child(userid).child("notifications").setValue(notifications);
        msgDataReference.child(userid).child(userid).child("timings").setValue(timings);
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


}

