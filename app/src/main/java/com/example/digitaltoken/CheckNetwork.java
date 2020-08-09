package com.example.digitaltoken;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CheckNetwork {

    public static boolean isNetworkConnected;
    private Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    public void myNetworkCheck() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            isOnline(context);
            if (isOnline(context)) {
                isNetworkConnected = true;
            } else {
                isNetworkConnected = false;
            }
        } else {
            registerNetworkCallback();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registerNetworkCallback() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                                                                   @Override
                                                                   public void onAvailable(Network network) {
                                                                       isNetworkConnected = true; // Global Static Variable
                                                                   }

                                                                   @Override
                                                                   public void onLost(Network network) {
                                                                       isNetworkConnected = false; // Global Static Variable
                                                                   }
                                                               }
            );
        } catch (Exception e) {
            isNetworkConnected = false;
        }
    }

    public boolean isOnline(Context context) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
