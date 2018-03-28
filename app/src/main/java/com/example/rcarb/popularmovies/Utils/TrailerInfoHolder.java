package com.example.rcarb.popularmovies.Utils;


import android.os.Parcel;
import android.os.Parcelable;

public class TrailerInfoHolder implements Parcelable {

    private String mTrailerKey;
    private String mTrailerName;

    public TrailerInfoHolder() {

    }

    protected TrailerInfoHolder(Parcel in) {
        mTrailerKey = in.readString();
        mTrailerName = in.readString();
    }

    public static final Creator<TrailerInfoHolder> CREATOR = new Creator<TrailerInfoHolder>() {
        @Override
        public TrailerInfoHolder createFromParcel(Parcel in) {
            return new TrailerInfoHolder(in);
        }

        @Override
        public TrailerInfoHolder[] newArray(int size) {
            return new TrailerInfoHolder[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTrailerKey);
        parcel.writeString(mTrailerName);
    }
}
