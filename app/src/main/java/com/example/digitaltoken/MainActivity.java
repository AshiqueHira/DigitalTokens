package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CardClickListner {

    Toolbar toolbar;

    RecyclerView myRecyclerView;
    private ArrayList<MyModel> models = new ArrayList<>();
    private RecyclerAdapter myRecyclerAdapter;

    EditText searchEditText;

    DatabaseReference dataReference;
    FirebaseUser firebaseUser;

    String name = "a";
    String location = "b";
    String userId = "c";

    List<String> list = new ArrayList<>();

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

        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRecyclerAdapter = new RecyclerAdapter(models, this);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        myRecyclerAdapter.notifyDataSetChanged();

        searchEditText = findViewById(R.id.searchEditText);

        dataReference = FirebaseDatabase.getInstance().getReference("Users");

        dataReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                name = dataSnapshot.getValue(User.class).getUserName();
                //Log.e("the error is ", String.valueOf(dataSnapshot.getValue()));
                location = dataSnapshot.getValue(User.class).getUserLocation();
                userId = dataSnapshot.getValue(User.class).getUsersId();

                Toast.makeText(MainActivity.this, name + " " + location + " " + userId, Toast.LENGTH_LONG).show();
                insertDatas(name, location, userId);
                myRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void insertDatas(String myname, String mylocation, String usersId) {


        MyModel m = new MyModel();
        m.setMyTitle(myname);
        m.setMyDisc(mylocation);
        m.setMyImg(R.drawable.homedr);
        m.setMyuid(usersId);

        models.add(m);


        /*m = new MyModel();
        m.setMyTitle("Dr. Salam MBBS, MD GENERAL MEDICINE");
        m.setMyDisc("Care Pharma, Orkkateri, Vadakara");
        m.setMyImg(R.drawable.clinic);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("Canara Bank");
        m.setMyDisc("Orkkateri, Vadakara");
        m.setMyImg(R.drawable.canara);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("Flour Mill");
        m.setMyDisc("OPK, Orkkateri");
        m.setMyImg(R.drawable.flour);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("Syndicate Bank");
        m.setMyDisc("Orkkateri, Vadakara");
        m.setMyImg(R.drawable.syndicate);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("Dr. Vijayan MBBS, MD GENERAL MEDICINE");
        m.setMyDisc("Home, Vellikulangara");
        m.setMyImg(R.drawable.homedr);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("SBI Bank");
        m.setMyDisc("Orkkateri, Vadakara");
        m.setMyImg(R.drawable.sbi);
        models.add(m);

        m = new MyModel();
        m.setMyTitle("Dr. Salam MBBS, MD GENERAL MEDICINE");
        m.setMyDisc("Sahakarana Hospital, Orkkateri");
        m.setMyImg(R.drawable.hospital);
        models.add(m);*/


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

    @Override
    public void onCardClick(int position) {
        String useroid = models.get(position).getMyuid();
        String moName = models.get(position).getMyTitle();
        String moLocation = models.get(position).getMyDisc();

        Intent intent = new Intent(this, TokenStatus.class);
        intent.putExtra("userId", useroid);
        intent.putExtra("name", moName);
        intent.putExtra("location", moLocation);
        startActivity(intent);
    }

}
