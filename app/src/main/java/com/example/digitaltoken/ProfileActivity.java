package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    DatabaseReference usersDataReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String userId = "";
    String userName = "";
    String userLocation = "";
    String userEmail = "";
    String userPhone = "";
    String inputEmail;

    EditText userNameET;
    EditText userLocationET;
    EditText userEmailET;
    EditText userPhoneET;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.myActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        userNameET = findViewById(R.id.userNameEditText);
        userLocationET = findViewById(R.id.userLocationEditText);
        userEmailET = findViewById(R.id.userEmailEditText);
        userPhoneET = findViewById(R.id.userPhoneEditText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        usersDataReference = FirebaseDatabase.getInstance().getReference("Users");

        usersDataReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(User.class).getUserName();
                userLocation = dataSnapshot.getValue(User.class).getUserLocation();
                userEmail = dataSnapshot.getValue(User.class).getUserEmail();
                userPhone = dataSnapshot.getValue(User.class).getUserPhone();

                userNameET.setText(userName);
                userLocationET.setText(userLocation);
                userEmailET.setText(userEmail);
                userPhoneET.setText(userPhone);

                userName = userNameET.getText().toString();
                userLocation = userLocationET.getText().toString();
                userEmail = userEmailET.getText().toString();
                userPhone = userPhoneET.getText().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void saveData(View view) {

        userName = userNameET.getText().toString();
        userLocation = userLocationET.getText().toString();
        userEmail = userEmailET.getText().toString();
        userPhone = userPhoneET.getText().toString();

        usersDataReference.child(userId).child("userName").setValue(userName);
        usersDataReference.child(userId).child("userLocation").setValue(userLocation);
        usersDataReference.child(userId).child("userEmail").setValue(userEmail);
        usersDataReference.child(userId).child("userPhone").setValue(userPhone);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.delete) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;

        }
        return false;
    }

    public void resetPasswordClick(View view) {

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
                                                Toast.makeText(ProfileActivity.this,
                                                        "The Reset link has send to your email provided",
                                                        Toast.LENGTH_SHORT).show();
                                                forgotBuilder.dismiss();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
