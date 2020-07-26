package com.example.digitaltoken;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckNetwork {

    public static boolean isNetworkConnected;
    private Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        isNetworkConnected = false;
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] allNetworks = connectivityMgr.getAllNetworks(); // added in API 21 (Lollipop)

        for (Network network : allNetworks) {
            NetworkCapabilities networkCapabilities = connectivityMgr.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    isNetworkConnected = true;
            }
            }
        return isNetworkConnected;
    }
}
