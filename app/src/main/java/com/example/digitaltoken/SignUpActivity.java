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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("email");
        String userPhone = intent.getStringExtra("phoneNumber");
        String userBusiness = intent.getStringExtra("bussinessType");
        finish();


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


    public void launchButton(View view) {

        if (myCity.equals("--Select Your District--") || myTown.equals("--Select Your Town--") || myStreet.equals("--Select Your Street--")) {
            Toast.makeText(this, "Please Fill your Location details completly", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, myCity + " " + myTown + " " + myStreet, Toast.LENGTH_SHORT).show();
        }




    }
}
