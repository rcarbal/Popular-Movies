package com.example.rcarb.popularmovies.Utils;

/**
 * Created by rcarb on 11/23/2017.
 */

public class TrailerInfoHolder {

    private String mTrailerKey;

    public TrailerInfoHolder(String trailerKey) {

        this.mTrailerKey = trailerKey;
    }

    public TrailerInfoHolder() {

    }

    public void setTrailerKey(String trailerKey) {
        this.mTrailerKey = trailerKey;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }
}
