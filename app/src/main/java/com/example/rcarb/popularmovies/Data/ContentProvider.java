package com.example.rcarb.popularmovies.Data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


@SuppressWarnings("ConstantConditions")
public class ContentProvider extends android.content.ContentProvider {

    //Integer constants that will be used on the Uri Matcher.
    private static final int FAVORITE_MOVIES_DATABASE = 100;
    private static final int FAVORITE_MOVIES_DATABSE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        //Empty matcher.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //matcher for all the movies in the database.
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES, FAVORITE_MOVIES_DATABASE);
        //matcher for one movie removed form the database.
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES +"/#", FAVORITE_MOVIES_DATABSE_WITH_ID);
        return uriMatcher;
    }

    //DBhelper class declaration.
    private FavoriteMovieDbHelper mMovieHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieHelper = new FavoriteMovieDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        //Get readable database
        final SQLiteDatabase db = mMovieHelper.getReadableDatabase();
        //Get the match uri from the matcher.
        int match = sUriMatcher.match(uri);

        Cursor cursor;
        //Switch statment to match the uri.
        switch (match) {
            case FAVORITE_MOVIES_DATABASE:
                cursor = db.query(
                        Contract.MovieEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            //Throw exeption on default
            default:
                throw new UnsupportedOperationException("Unknown ur: " + uri);
        }
        //tell the cursor what content uri it was created for.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {
        //Get writable database.
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        //get uri match
        int match = sUriMatcher.match(uri);
        //Variable that will store the returned uri from the switch statement.
        Uri returnedUri;
        ContentValues contentValues = new ContentValues(values);

        switch (match) {
            case FAVORITE_MOVIES_DATABASE:
                long id = db.insert(Contract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    //Success
                    returnedUri = ContentUris.withAppendedId(Contract.MovieEntry.BASE_CONTENT_URI_FAVORITES, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //Notify the resolver that a change has occured in a particular uri.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        //Get writable database.
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        //get uri match.
        int match = sUriMatcher.match(uri);
        //number of deleted movies.
        int moviesDeleted;
        switch (match) {
            case FAVORITE_MOVIES_DATABSE_WITH_ID:
                //get id from the uri.
                String id = uri.getPathSegments().get(1);
                //Use selection/selectionArgs to filter for this id.
                moviesDeleted = db.delete(Contract.MovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Notify the resolver of a change.
        if (moviesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
