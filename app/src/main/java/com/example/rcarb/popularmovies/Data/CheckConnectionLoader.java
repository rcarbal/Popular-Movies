package com.example.rcarb.popularmovies.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

//Checks if there is network connection.

public class CheckConnectionLoader extends AsyncTaskLoader<Boolean> {
    public CheckConnectionLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressWarnings("ConstantConditions")
        NetworkInfo activeNetwork= cm.getActiveNetworkInfo();
        @SuppressWarnings("UnnecessaryLocalVariable") boolean
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
