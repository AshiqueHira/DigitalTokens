package com.example.digitaltoken;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckNetwork {


    public void isNetwork(final Context context) {
        ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {

                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(Network network) {

            }
        };
    }

}
