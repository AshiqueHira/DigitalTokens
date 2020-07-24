package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    String name = "name";
    String location = "location";
    String userId = "c";
    String userBusiness = "default";
    String userImage = "noimage";

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
        getSupportActionBar().setTitle("itoken");

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

                // setting up image in recyclerview
                userBusiness = dataSnapshot.getValue(User.class).getUserBusiness();
                if (userBusiness.equals("Doctor(Self Service)")) {
                    userImage = "doctor";
                } else if (userBusiness.equals("Ration Shop")) {
                    userImage = "ration";
                } else if (userBusiness.equals("Sales Shop")) {
                    userImage = "sales";
                } else if (userBusiness.equals("Flour Mill")) {
                    userImage = "flour";
                } else if (userBusiness.equals("Govt. Hospital")) {
                    userImage = "govermenthospital";
                } else if (userBusiness.equals("Bank")) {
                    userImage = "bank";
                }

                insertDatas(name, location, userId, userImage);
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

    public void insertDatas(String myname, String mylocation, String usersId, String imagefile) {


        MyModel m = new MyModel();
        m.setMyTitle(myname);
        m.setMyDisc(mylocation);
        m.setMyImg(getResources().getIdentifier(imagefile, "drawable", getPackageName()));
        //m.setMyImg(R.drawable.homedr);
        m.setMyuid(usersId);

        models.add(m);


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

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);


        boolean isMetered = conMgr.isActiveNetworkMetered();
        if (isMetered) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
