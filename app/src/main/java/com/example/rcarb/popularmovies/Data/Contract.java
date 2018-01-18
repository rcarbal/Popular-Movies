package com.example.rcarb.popularmovies.Data;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Thihs contract class for inserting data into the the SQLdatabase.
 */

@SuppressWarnings("WeakerAccess")
public class Contract {

    //Authority for the uri matcher.
    public static final String AUTHORITY = "com.example.rcarb.popularmovies";

    //String for accessing the favorite table.
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "Favorites_Movies";

    public static class MovieEntry implements BaseColumns {
        //Urii that ponts to the favorites table.
        public static final Uri BASE_CONTENT_URI_FAVORITES=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = " Favorite_Movies";
        public static final String COLUMN_MOVIE_TITLE = "Title";
        public static final String COLUMN_MOVIE_POSTER = "Poster";
        public static final String COLUMN_MOVIE_YEAR = "year";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_DESCRIPTION = "description";
        public static final String COLUMN_MOVIE_ID = "movie_id";


        //Create database for a column
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_YEAR + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_DESCRIPTION + " TEXT NOT NULL)";

        //Deletes the databse
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
    }
}


