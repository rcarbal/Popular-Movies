package com.example.rcarb.popularmovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rcarb on 1/18/2018.
 */

public class CheckNetworkConnection {

    public static Boolean checkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressWarnings("ConstantConditions")
        NetworkInfo activeNetwork= cm.getActiveNetworkInfo();
        @SuppressWarnings("UnnecessaryLocalVariable") boolean
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}

