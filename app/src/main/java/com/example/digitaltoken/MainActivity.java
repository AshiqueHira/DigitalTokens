package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import static android.graphics.ColorSpace.*;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.myActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Digital Token");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);

        //EditText editText = findViewById(R.id.editText);

    }

    public ArrayList<Model> getMyList() {

        ArrayList<Model> models = new ArrayList<Model>();


        Model m = new Model();
        m.setTitleList("Ration Shop");
        m.setDiscList("Orkkateri, Vadakara");
        m.setImg(R.drawable.rationcard);

        models.add(m);


        m = new Model();
        m.setTitleList("Dr. Salam MBBS, MD GENERAL MEDICINE");
        m.setDiscList("Care Pharma, Orkkateri, Vadakara");
        m.setImg(R.drawable.clinic);
        models.add(m);

        m = new Model();
        m.setTitleList("Canara Bank");
        m.setDiscList("Orkkateri, Vadakara");
        m.setImg(R.drawable.canara);
        models.add(m);

        m = new Model();
        m.setTitleList("Flour Mill");
        m.setDiscList("OPK, Orkkateri");
        m.setImg(R.drawable.flour);
        models.add(m);

        m = new Model();
        m.setTitleList("Syndicate Bank");
        m.setDiscList("Orkkateri, Vadakara");
        m.setImg(R.drawable.syndicate);
        models.add(m);

        m = new Model();
        m.setTitleList("Dr. Vijayan MBBS, MD GENERAL MEDICINE");
        m.setDiscList("Home, Vellikulangara");
        m.setImg(R.drawable.homedr);
        models.add(m);

        m = new Model();
        m.setTitleList("SBI Bank");
        m.setDiscList("Orkkateri, Vadakara");
        m.setImg(R.drawable.sbi);
        models.add(m);

        m = new Model();
        m.setTitleList("Dr. Salam MBBS, MD GENERAL MEDICINE");
        m.setDiscList("Sahakarana Hospital, Orkkateri");
        m.setImg(R.drawable.hospital);
        models.add(m);
        return models;

    }


    public boolean onCreateOptionMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.admin) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            return true;
        }
        return false;
    }


}
