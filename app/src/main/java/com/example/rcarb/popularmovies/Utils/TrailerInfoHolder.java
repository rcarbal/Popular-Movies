package com.example.rcarb.popularmovies.Utils;


public class TrailerInfoHolder {

    private String mTrailerKey;
    private String mTrailerName;

    public TrailerInfoHolder() {

    }

    public void setTrailerKey(String trailerKey) {
        this.mTrailerKey = trailerKey;
    }
    public String getTrailerKey() {
        return mTrailerKey;
    }

    public void setTrailerName(String trailerName){
        mTrailerName = trailerName;
    }
    public String getTrailerName(){
        return mTrailerName;
    }


}
