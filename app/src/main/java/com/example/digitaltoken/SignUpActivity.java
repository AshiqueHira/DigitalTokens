package com.example.digitaltoken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText;

    Spinner citySpinner;
    Spinner townSpinner;
    Spinner streetSpinner;
    String myCity = "--Select Your District--";
    String myTown = "--Select Your Town--";
    String myStreet = "--Select Your Street--";
    boolean errorToast = true;

    DatabaseReference usersDataReference;
    DatabaseReference msgDataReference;
    FirebaseUser user;

    //////////////////////// initialise strings and stuffs
    String userid = "A";
    String userEmail = "A";
    String userPhone = "A";
    String userBusiness = "A";
    String userName = "A";

    String counter = "b";
    String timings = "b";
    String notifications = "b";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPhone = intent.getStringExtra("phoneNumber");
        userBusiness = intent.getStringExtra("bussinessType");

        Toast.makeText(this, userEmail + "  " + userPhone + "  " + userBusiness, Toast.LENGTH_LONG).show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        usersDataReference = FirebaseDatabase.getInstance().getReference("Users");
        msgDataReference = FirebaseDatabase.getInstance().getReference("Messages");
        userid = user.getUid();
        nameEditText = findViewById(R.id.nameEditText);


        citySpinner = findViewById(R.id.citySpinner);
        townSpinner = findViewById(R.id.townSpinner);
        streetSpinner = findViewById(R.id.streetSpinner);

        final String city[] = {"--Select Your District--", "Kozhikkode"};

        final String kozhikkodeCity[] = {"--Select Your Town--", "vadakara", "Nadapuram"};
        final String vadakaraTown[] = {"--Select Your Street--", "Orkkateri", "vellikulangara"};
        final String nadapuramTown[] = {"--Select Your Street--", "Edacheri", "Purameri"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myCity = city[position];
                Toast.makeText(SignUpActivity.this, myCity, Toast.LENGTH_SHORT).show();

                if (position == 1) {
                    ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_dropdown_item, kozhikkodeCity);
                    townSpinner.setAdapter(townAdapter);

                }
                townSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        myTown = kozhikkodeCity[position];
                        Toast.makeText(SignUpActivity.this, myTown, Toast.LENGTH_SHORT).show();
                        if (position == 1) {
                            ArrayAdapter<String> streetAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_dropdown_item, vadakaraTown);
                            streetSpinner.setAdapter(streetAdapter);

                        } else if (position == 2) {
                            ArrayAdapter<String> streetAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_dropdown_item, nadapuramTown);
                            streetSpinner.setAdapter(streetAdapter);
                        }
                        streetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                myStreet = streetSpinner.getSelectedItem().toString();
                                Toast.makeText(SignUpActivity.this, myStreet, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    String location = myCity + " " + myTown + " " + myStreet;
    public void launchButton(View view) {

        if (myCity.equals("--Select Your District--") || myTown.equals("--Select Your Town--") || myStreet.equals("--Select Your Street--")) {
            Toast.makeText(this, "Please Fill your Location details completly", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, myCity + " " + myTown + " " + myStreet, Toast.LENGTH_SHORT).show();
            location = myStreet + " " + myTown + " " + myCity;
            addUsers();
            Intent intento = new Intent(getApplicationContext(), AdminActivity.class);
            intento.putExtra(userName, "userName");
            intento.putExtra(userEmail, "userEmail");
            intento.putExtra(location, "location");
            intento.putExtra(userBusiness, "userBusiness");
            intento.putExtra(userPhone, "userPhone");
            startActivity(intento);

        }

    }

    public void addUsers() {
        userName = nameEditText.getText().toString();
        User user = new User(userid, userEmail, userPhone, userBusiness, location, userName);
        usersDataReference.child(userid).setValue(user);

        Message message = new Message(userid, timings, counter, notifications);
        msgDataReference.child(userid).setValue(message);
    }
}
