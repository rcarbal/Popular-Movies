package com.example.rcarb.popularmovies.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rcarb on 11/10/2017.
 */

public class FavoriteMovieList implements Parcelable {

    private final String mMovieName;
    private final int mColumnId;

    FavoriteMovieList(String movieName, int columnId) {
        this.mMovieName = movieName;
        this.mColumnId = columnId;

    }

    private FavoriteMovieList(Parcel in) {
        mMovieName = in.readString();
        mColumnId = in.readInt();
    }

    public static final Creator<FavoriteMovieList> CREATOR = new Creator<FavoriteMovieList>() {
        @Override
        public FavoriteMovieList createFromParcel(Parcel in) {
            return new FavoriteMovieList(in);
        }

        @Override
        public FavoriteMovieList[] newArray(int size) {
            return new FavoriteMovieList[size];
        }
    };

    public String getmMovieName() {
        return mMovieName;
    }

    public int getmColumnId() {
        return mColumnId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieName);
        dest.writeInt(mColumnId);
    }
}
