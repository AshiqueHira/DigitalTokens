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

    EditText clinicEditText;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;
    ///////////  initialisation

    String businessType = "Select Business Type";

    String email = "B";
    String phone = "B";

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

        firebaseAuth = FirebaseAuth.getInstance();

        final List<String> list = new ArrayList<>();

        list.add("Select Business Type");
        list.add("Ration Shop");
        list.add("Doctor(Self Service)");
        list.add("Sales Shop");
        //list.add("Clinic");
        list.add("Flour Mill");
        list.add("Govt. Hospital");
        //list.add("Hospital");
        list.add("Bank");

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

                //String selectedItem = dropdownmenu.getSelectedItem().toString();
                //Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                //intent.putExtra("businessType",selectedItem);

                email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

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

                if (businessType.equals("Select Business Type")) {
                    Toast.makeText(TypeActivity.this, "Please Select the Business Type", Toast.LENGTH_LONG).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                // register the user in firebase

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TypeActivity.this, "The User is Successfully Created", Toast.LENGTH_SHORT).show();

                            Intent intented = new Intent(getApplicationContext(), SignUpActivity.class);
                            intented.putExtra("email", email);
                            intented.putExtra("phoneNumber", phone);
                            intented.putExtra("bussinessType", businessType);


                            startActivity(intented);


                        } else {
                            Toast.makeText(TypeActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });


    }


}
