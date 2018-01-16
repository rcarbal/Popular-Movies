package com.example.rcarb.popularmovies.Data;

/**
 * Created by rcarb on 11/4/2017.
 */

import android.provider.BaseColumns;

/**
 * Thihs contract class for inserting data into the the SQLdatabase.
 */

public class Contract {

    private Contract() {

    }

    public static class MovieEntry implements BaseColumns {

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


