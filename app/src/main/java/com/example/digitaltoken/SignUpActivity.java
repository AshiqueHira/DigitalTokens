package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText addressEditText;

    Spinner districtSpinner;
    AutoCompleteTextView townACTV;
    AutoCompleteTextView localityACTV;

    ProgressBar progressBar;

    String myDistrict = "--Select Your District--";
    String myTown = "";
    String myLocality = "";
    String location = "";

    DatabaseReference usersDataReference;
    DatabaseReference msgDataReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    //////////////////////// initialise strings and stuffs
    String userid = "A";
    String userEmail = "A";
    String userPhone = "A";
    String userBusiness = "A";
    String userName = "A";
    String password = "A";
    String address = "";

    String counter = "0";
    String timings = "...";
    String notifications = "No Notifications Yet";
    String sAvgToken = "0";

    boolean launchGo = true;

    CheckNetwork myNetwork = new CheckNetwork(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPhone = intent.getStringExtra("phoneNumber");
        userBusiness = intent.getStringExtra("bussinessType");
        password = intent.getStringExtra("password");

        myNetwork.myNetworkCheck();
        if (!CheckNetwork.isNetworkConnected) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        usersDataReference = FirebaseDatabase.getInstance().getReference("Users");
        msgDataReference = FirebaseDatabase.getInstance().getReference("Messages");
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        townACTV = findViewById(R.id.townACTV);
        localityACTV = findViewById(R.id.streetACTV);
        districtSpinner = findViewById(R.id.districtSpinner);
        progressBar = findViewById(R.id.progressBarsignup);
        progressBar.setVisibility(View.GONE);

        addressEditText.setVisibility(View.GONE);

        if (userBusiness.equals("Bank")) {
            nameEditText.setHint("Bank Name");
        } else if (userBusiness.equals("Doctor(Home Service)")) {
            nameEditText.setHint("Name example: Dr. John MBBS, MD, General Medicine");

        } else if (userBusiness.equals("Doctor(Clinic Service)")) {
            addressEditText.setVisibility(View.VISIBLE);
            nameEditText.setHint("Dr. Name Example: Dr. John MBBS, MD, General Medicine");
            addressEditText.setHint("Clinic Name");

        } else if (userBusiness.equals("Doctor(Hospital Service)")) {
            addressEditText.setVisibility(View.VISIBLE);
            nameEditText.setHint("Dr. Name Example: Dr. John MBBS, MD, General Medicine");
            addressEditText.setHint("Hospital Name");

        } else if (userBusiness.equals("Flour Mill")) {
            nameEditText.setHint("Title Name");

        } else if (userBusiness.equals("Govt. Hospital")) {
            nameEditText.setHint("Hospital Name");

        } else if (userBusiness.equals("Ration Shop")) {
            nameEditText.setHint("Title Name");

        } else if (userBusiness.equals("Sales Shop")) {
            nameEditText.setHint("Shop Name");
        }

        progressBar = findViewById(R.id.progressBarr);
        progressBar.setVisibility(View.INVISIBLE);

        final String[] district = {"--Select Your District--", "Alappuzha", "Ernakulam", "Idukki",
                "Kannur", "Kasaragod", "Kollam", "Kottayam", "Kozhikkode", "Malappuram", "Palakkad",
                "Pathanamthitta", "Thiruvananthapuram", "Thrissur", "Wayanad"};

        final String[] towns = {"--Select Your Town--", "Adoor", "Alathur", "Aluva", "Ambalapuzha", "Attingal",
                "Chalakudy", "Changanasserry", "Chathannoor", "Chavakkad", "Chengannur", "Cherthala",
                "Chirayinkeezhu", "Chittur", "Devikulam", "Eranad", "Ernakulam", "Fort Kochi", "Haripad",
                "Hosdurg", "Idukki", "Irinjalakuda", "Iritty", "Kalpetta", "Kanayannur", "Kanjirappally",
                "Kannur", "Karthikappally", "Karunagappally", "Kasaragod", "Kattakada", "Kochi",
                "Kodungallur", "Kollam", "Kondotty", "Konni", "Kothamangalam", "Kottarakkara",
                "Kottayam", "Koyilandy", "Kozhencherry", "Kozhikode", "Kunnamkulam", "Kunnathunad",
                "Kunnathur", "Kuttanad", "Mallappally", "Mananthavady", "Manjeri", "Manjeshwaram",
                "Mankombu", "Mannarkkad", "Mavelikkara", "Meenachil", "Mukundapuram", "Muvattupuzha",
                "Nedumangad", "Nedumkandam", "Neyyattinkara", "Nilambur", "North Paravur", "Ottappalam",
                "Painavu", "Palai", "Palakkad", "Paravur", "Pathanamthitta", "Pathanapuram", "Pattambi",
                "Payyanur", "Peermade", "Perinthalmanna", "Perumbavoor", "Ponnani", "Punalur", "Ranni",
                "Sasthamkotta", "Sultan Battery", "Thalapilly", "Thalassery", "Thalipparamba",
                "Thamarassery", "Thiruvalla", "Thiruvananthapuram", "Thodupuzha", "Thrissur",
                "Tirur", "Tirurangadi", "Udumbanchola", "Uppala", "Vaikom", "Varkala", "Vatakara",
                "Vellarikundu", "Vythiri", "Wadakkancheri"};

        final String[] localities = {"--Select Your Street--", "Orkkateri", "vellikulangara"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, district);
        districtSpinner.setAdapter(cityAdapter);

        ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, towns);
        townACTV.setAdapter(townAdapter);
        townACTV.getCompletionHint();
        townACTV.setThreshold(1);

        ArrayAdapter<String> localityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, localities);
        localityACTV.setAdapter(localityAdapter);
        localityACTV.getCompletionHint();
        localityACTV.setThreshold(1);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myDistrict = district[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void networkMethod() {
        myNetwork.myNetworkCheck();
        if (!CheckNetwork.isNetworkConnected) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            launchGo = false;
        } else {
            launchGo = true;
        }
    }

    public void launchButton(View view) {
        networkMethod();
        if (launchGo) {
            progressBar.setVisibility(View.VISIBLE);
            launchGo = false;

            myTown = townACTV.getText().toString();
            myLocality = localityACTV.getText().toString();
            address = addressEditText.getText().toString();

            if (TextUtils.isEmpty(myTown)) {
                townACTV.setError("Enter Your Town");
                return;
            }
            if (TextUtils.isEmpty(myLocality)) {
                localityACTV.setError("Enter Your Locality");
                return;
            }

            if (myDistrict.equals("--Select Your District--") || TextUtils.isEmpty(myTown) || TextUtils.isEmpty(myLocality)) {
                Toast.makeText(this, "Please Fill your Location details completly", Toast.LENGTH_LONG).show();
            } else {
                if (!TextUtils.isEmpty(address)) {
                    location = address + ", " + myLocality + ", " + myTown;
                } else {
                    location = myLocality + ", " + myTown + ", " + myDistrict;
                }
                firebaseAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "The User is Successfully Created", Toast.LENGTH_SHORT).show();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            userid = user.getUid();
                            addUsers();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            launchGo = true;
                        }
                    }
                });
            }
        }
    }

    public void addUsers() {
        userName = nameEditText.getText().toString();
        User user = new User(userid, userEmail, userPhone, userBusiness, location, userName);
        usersDataReference.child(userid).setValue(user);
        Message message = new Message(userid, timings, counter, notifications, sAvgToken);
        msgDataReference.child(userid).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intento = new Intent(SignUpActivity.this, AdminActivity.class);
                    startActivity(intento);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    launchGo = true;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
