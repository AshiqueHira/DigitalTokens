package com.example.digitaltoken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
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


public class MainActivity extends AppCompatActivity implements CardClickListner, SearchView.OnQueryTextListener {

    Toolbar toolbar;

    RecyclerView myRecyclerView;
    private ArrayList<MyModel> models = new ArrayList<>();
    private RecyclerAdapter myRecyclerAdapter;

    ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    DatabaseReference dataReference;
    FirebaseUser firebaseUser;

    String name = "name";
    String location = "location";
    String userId = "c";
    String userBusiness = "default";
    String userImage = "noimage";

    List<String> list = new ArrayList<>();

    CheckNetwork myNetwork = new CheckNetwork(this);

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

        if (!myNetwork.isNetworkConnected) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        shimmerFrameLayout = findViewById(R.id.myShimmo);
        myRecyclerView = findViewById(R.id.recyclerView);


        myRecyclerView.setVisibility(View.INVISIBLE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRecyclerAdapter = new RecyclerAdapter(models, this);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        myRecyclerAdapter.notifyDataSetChanged();


        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                myRecyclerAdapter.notifyDataSetChanged();
                if (!CheckNetwork.isNetworkConnected) {
                    Toast.makeText(MainActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }


            }

        });*/

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
                } else if (userBusiness.equals("Doctor(Clinic Service)")) {
                    userImage = "clinic";
                } else if (userBusiness.equals("Doctor(Hospital Service)")) {
                    userImage = "hospital";
                } else if (userBusiness.equals("Ration Shop")) {
                    userImage = "ration";
                } else if (userBusiness.equals("Sales Shop")) {
                    userImage = "cart";
                } else if (userBusiness.equals("Flour Mill")) {
                    userImage = "flour";
                } else if (userBusiness.equals("Govt. Hospital")) {
                    userImage = "hospital";
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
        m.setMyuid(usersId);

        models.add(m);
        if (!models.isEmpty()) {
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            myRecyclerView.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.admin) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(this);
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<MyModel> newList = new ArrayList<>();

        for (MyModel name : models) {
            if (name.getMyTitle().toLowerCase().contains(userInput)) {
                newList.add(name);
            } else if (name.getMyDisc().toLowerCase().contains(userInput)) {
                newList.add(name);
            }
        }
        myRecyclerAdapter.updateList(newList);
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }
}
