package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TypeActivity extends AppCompatActivity {

    Spinner dropdownmenu;

    Button registerButton;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmEditText;
    EditText phoneEditText;

    ///////////  initialisation

    String businessType = "Select Facility Type";
    String email = "";
    String phone = "";
    String password = "";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        dropdownmenu = findViewById(R.id.spinner);

        registerButton = findViewById(R.id.registerButton);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmEditText = findViewById(R.id.confirmEditText);
        phoneEditText = findViewById(R.id.phoneEditText);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        final List<String> list = new ArrayList<>();

        list.add("Select Facility Type");
        list.add("Bank");
        list.add("Doctor(Home Service)");
        list.add("Doctor(Clinic Service)");
        list.add("Doctor(Hospital Service)");
        list.add("Flour Mill");
        list.add("Govt. Hospital");
        list.add("Ration Shop");
        list.add("Sales Shop");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownmenu.setAdapter(arrayAdapter);

        dropdownmenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                businessType = list.get(position);
                Toast.makeText(TypeActivity.this, businessType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                String confirm = confirmEditText.getText().toString().trim();
                phone = phoneEditText.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is Required");
                    return;
                }
                if (password.length() < 8) {
                    passwordEditText.setError("Your password must be atleast 8 characters");
                    return;
                }

                if (TextUtils.isEmpty(confirm) || !password.equals(confirm)) {
                    confirmEditText.setError("Your password confirmation failed");
                    return;
                }

                if (businessType.equals("Select Facility Type")) {
                    Toast.makeText(TypeActivity.this, "Please Select the Facility Type", Toast.LENGTH_LONG).show();
                    return;
                }


                Intent intented = new Intent(getApplicationContext(), SignUpActivity.class);
                intented.putExtra("email", email);
                intented.putExtra("phoneNumber", phone);
                intented.putExtra("bussinessType", businessType);
                intented.putExtra("password", password);
                startActivity(intented);

            }
        });


    }


}
