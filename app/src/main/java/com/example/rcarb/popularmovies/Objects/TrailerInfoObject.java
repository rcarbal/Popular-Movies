package com.example.rcarb.popularmovies.Objects;


import android.os.Parcel;
import android.os.Parcelable;

public class TrailerInfoObject implements Parcelable {
    private String mTrailerKey;
    private String mTrailerName;

    public TrailerInfoObject() {

    }

    protected TrailerInfoObject(Parcel in) {
        mTrailerKey = in.readString();
        mTrailerName = in.readString();
    }

    public static final Creator<TrailerInfoObject> CREATOR = new Creator<TrailerInfoObject>() {
        @Override
        public TrailerInfoObject createFromParcel(Parcel in) {
            return new TrailerInfoObject(in);
        }

        @Override
        public TrailerInfoObject[] newArray(int size) {
            return new TrailerInfoObject[size];
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
