package com.example.rcarb.popularmovies.Utils;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Java class that has getter and setter methods to hold movie information.
 */
public class MovieInfoHolder implements Parcelable {
    private int movieId;
    private String moviePoster;
    private String movieTitle;
    private String movieReleaseDate;
    private int movieLength;
    private String movieRating;
    private String movieDescription;
    private boolean mFavorite;
    private long databaseColumn;
    private String mListDescription;

    public MovieInfoHolder() {

    }

    public MovieInfoHolder(int movieId,
                           String moviePoster,
                           String movieTitle,
                           String movieReleaseDate,
                           int movieLength,
                           String movieRating,
                           String movieDescription,
                           boolean isFavorite,
                           long movieDatabaseColumn,
                           String listDescription) {


        this.movieId = movieId;
        this.moviePoster = moviePoster;
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieLength = movieLength;
        this.movieRating = movieRating;
        this.movieDescription = movieDescription;
        this.mFavorite = isFavorite;
        this.databaseColumn = movieDatabaseColumn;
        this.mListDescription = listDescription;
    }


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

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public int getMovieLength() {
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
            return in.readInt() == 1 ? true : false;
        }
        return false;
    }

    public long getColumn() {
        return this.databaseColumn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(moviePoster);
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeInt(movieLength);
        dest.writeString(movieRating);
        dest.writeString(movieDescription);
        writeBoolean(dest, mFavorite);
        dest.writeLong(databaseColumn);
        dest.writeString(mListDescription);
    }

    //Parcel Creator
    public static final Parcelable.Creator CREATOR
            = new ClassLoaderCreator() {
        @Override
        public MovieInfoHolder createFromParcel(Parcel in) {
            return new MovieInfoHolder(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }

        @Override
        public Object createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }
    };

    private MovieInfoHolder(Parcel in) {
        movieId = in.readInt();
        moviePoster = in.readString();
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        movieLength = in.readInt();
        movieRating = in.readString();
        movieDescription = in.readString();
        mFavorite = readBoolean(in);
    }
}

