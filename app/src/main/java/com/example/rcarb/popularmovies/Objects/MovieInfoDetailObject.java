package com.example.rcarb.popularmovies.Objects;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Java class that has getter and setter methods to hold movie information.
 */
@SuppressWarnings({"WeakerAccess", "unused", "SimplifiableIfStatement"})
public class MovieInfoDetailObject implements Parcelable{
    private int movieId =-1;
    private String moviePoster ="";
    private String movieTitle="";
    private String movieReleaseDate="";
    private String movieLength="";
    private String movieRating="";
    private String movieDescription="";
    private boolean mFavorite;
    private long databaseColumn=-1;
    private String mListDescription="";

    public MovieInfoDetailObject() {
    }


    protected MovieInfoDetailObject(Parcel in) {
        movieId = in.readInt();
        moviePoster = in.readString();
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        movieLength = in.readString();
        movieRating = in.readString();
        movieDescription = in.readString();
        mFavorite = in.readByte() != 0;
        databaseColumn = in.readLong();
        mListDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(moviePoster);
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieLength);
        dest.writeString(movieRating);
        dest.writeString(movieDescription);
        dest.writeByte((byte) (mFavorite ? 1 : 0));
        dest.writeLong(databaseColumn);
        dest.writeString(mListDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfoDetailObject> CREATOR = new Creator<MovieInfoDetailObject>() {
        @Override
        public MovieInfoDetailObject createFromParcel(Parcel in) {
            return new MovieInfoDetailObject(in);
        }

        @Override
        public MovieInfoDetailObject[] newArray(int size) {
            return new MovieInfoDetailObject[size];
        }
    };

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
    }

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setFavorite(boolean setBoolean) {
        this.mFavorite = setBoolean;
    }

    public boolean getFavorite() {
        return mFavorite;
    }

    public void setColumn(long column) {
        this.databaseColumn = column;
    }

    public long getColumn() {
        return this.databaseColumn;
    }

    public void setListDescription(String descriptionType) {

        this.mListDescription = descriptionType;
    }
    public String getListDescrition() {
        return mListDescription;
    }

    //Speceial method that writes Boolean to Parcel.
    private static void writeBoolean(Parcel destination, boolean value) {
        if (destination != null) {
            destination.writeInt(value ? 0 : 1);
        }
    }

    //Special method that reads boolean from parcel
    private static boolean readBoolean(Parcel in) {
        if (in != null) {
            return in.readInt() == 1;
        }
        return false;
    }




}

