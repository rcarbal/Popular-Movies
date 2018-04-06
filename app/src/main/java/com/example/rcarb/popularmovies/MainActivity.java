package com.example.rcarb.popularmovies;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.rcarb.popularmovies.Data.CheckConnectionLoader;
import com.example.rcarb.popularmovies.Data.Contract;
import com.example.rcarb.popularmovies.Objects.MovieInfoDetailObject;
import com.example.rcarb.popularmovies.Utils.CheckNetworkConnection;
import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


@SuppressWarnings({"ConstantConditions", "StatementWithEmptyBody", "UnusedAssignment"})
public class MainActivity extends AppCompatActivity
        implements GridViewAdapter.OnItemClicked {

    private final static int CHECK_CONNECTION = 1;
    private static final String DB_FULL_PATH = "/data/data/com.example.rcarb.popularmovies/databases/FavoriteMovies.db";

    private RecyclerView mRecyclerView;
    private ArrayList<MovieInfoDetailObject> mCurrentMovies;
    private FetchMovieTask task;
    private Context mContext;
    private Cursor mCursor;
    private String[] mMoviesArray;
    private String stateOfActivity = "";
    private boolean favoriteState = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("favorite", favoriteState);
        outState.putString("stateOfActivity", stateOfActivity);
        outState.putParcelableArrayList("movie_array", mCurrentMovies);
        outState.putStringArray("string_array", mMoviesArray);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            stateOfActivity = savedInstanceState.getString("stateOfActivity");
            favoriteState = savedInstanceState.getBoolean("favorite");
            mCurrentMovies = savedInstanceState.getParcelableArrayList("movie_array");
            mMoviesArray = savedInstanceState.getStringArray("string_array");

        }

        if (checkDataBaseExists()) {
            checkCursorData();

        }
        mContext = MainActivity.this;
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        task = new FetchMovieTask(mContext, mRecyclerView);

        callNetwork();

    }

    //Returns int to dynamically calculate for a layout of varied screen sizes
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        float xdpi = displayMetrics.xdpi;
        int getInches = Math.round(width / xdpi);
        if (getInches < 3) return 2;
        int widthDivider = 500;
        return width / widthDivider;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular) {
            FetchMovieTask task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute("popular");
        }
        if (id == R.id.top_rated) {
            FetchMovieTask task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute("top_rated");
        }
        if (id == R.id.favorite_database) {
            FetchMovieTask task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute("favorites");
        }
        return super.onOptionsItemSelected(item);
    }

    private void callNetwork() {
        if (stateOfActivity.equals("")) {
            task.execute("initial");
        } else if (stateOfActivity.equals("popular")) {
            task.execute("popular");
        } else if (stateOfActivity.equals("top_rated")) {
            task.execute("top_rated");
        } else if (stateOfActivity != null && favoriteState) {
            task.execute("favorites");
        }
    }


    //Checks to see if a contract already exists
    private boolean checkDataBaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    //Reads the databse of favorite movies
    private Cursor getAllFavoriteMovies() {
        Cursor cursor;
        cursor = getContentResolver().query(Contract.MovieEntry.BASE_CONTENT_URI_FAVORITES,
                null,
                null,
                null,
                null);
        return cursor;
    }

    //Gets the row of a movies title is located in the database.
    private long getMovieIdColumn(int movieId) {
        int passedinMovieId;
        long returnedMovieIdColumn = -1;

        if (!checkCursorData()) {
            return -1;
        }
        if (mCursor.moveToFirst()) {
            do {
                passedinMovieId = mCursor.getInt(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_ID));
                if (passedinMovieId == movieId) {
                    returnedMovieIdColumn = mCursor.getLong(mCursor.getColumnIndex(Contract.MovieEntry._ID));
                }
            } while (mCursor.moveToNext());
        }
        return returnedMovieIdColumn;
    }


    //Checks to see if the global cursor is holding any movie info
    private boolean checkCursorData() {
        Cursor cursor = getAllFavoriteMovies();
        if (cursor != null && cursor.getCount() > 0) {
            mCursor = cursor;
            return true;
        }
        return false;
    }

    private ArrayList<MovieInfoDetailObject> getListOfMovies(String typeOfMovie) {
        ArrayList<MovieInfoDetailObject> movies = new ArrayList<>();
        for (int i = 0; i < mCurrentMovies.size(); i++) {
            if (mCurrentMovies.get(i).getListDescrition().equals(typeOfMovie)) {
                movies.add(mCurrentMovies.get(i));
            }
        }
        return movies;
    }

    private int getIndexOfCurrentMovies(int movieId) {
        int index = -1;
        for (int i = 0; i < mCurrentMovies.size(); i++) {
            if (mCurrentMovies.get(i).getMovieId() == movieId) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onItemClick(int mNumberOfItemsIndex, int movieId) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        MovieInfoDetailObject holder = mCurrentMovies.get(getIndexOfCurrentMovies(movieId));
        long id = getMovieIdColumn(movieId);
        if (id == -1) {

        } else {
            holder.setFavorite(true);
            holder.setColumn(id);
        }
        intent.putExtra("movie", holder);
        startActivity(intent);
    }

    //Converts the current Cursor data into an arraylist.
    private ArrayList<MovieInfoDetailObject> convertCursorToMovienfoHolder() {
        int movieId;
        String moviePoster;
        String movieTitle;
        String movieReleaseDate;
        int movieLength;
        String movieRating;
        String movieDescription;
        boolean mFavorite;
        long databaseColumn;

        ArrayList<MovieInfoDetailObject> movieArray = new ArrayList<>();


        if (mCursor.moveToFirst()) {

            do {
                for (int i = 0; i < mCursor.getCount(); i++) {

                    movieId = mCursor.getInt(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_ID));
                    moviePoster = mCursor.getString(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_POSTER));
                    movieTitle = mCursor.getString(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_TITLE));
                    movieReleaseDate = mCursor.getString(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_YEAR));
                    movieLength = -1;
                    movieRating = mCursor.getString(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_RATING));
                    movieDescription = mCursor.getString(mCursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIE_DESCRIPTION));
                    mFavorite = true;
                    databaseColumn = mCursor.getLong(mCursor.getColumnIndex(Contract.MovieEntry._ID));

                    //Add the movie values into MovieInfoDetailObject.
                    MovieInfoDetailObject movieInfo = new MovieInfoDetailObject();
                    movieInfo.setMovieId(movieId);
                    movieInfo.setMoviePoster(moviePoster);
                    movieInfo.setMovieTitle(movieTitle);
                    movieInfo.setMovieReleaseDate(movieReleaseDate);

                    movieInfo.setMovieRating(movieRating);
                    movieInfo.setMovieDescription(movieDescription);
                    movieInfo.setFavorite(mFavorite);
                    movieInfo.setColumn(databaseColumn);

                    movieArray.add(i, movieInfo);
                    mCursor.moveToNext();
                }

            } while (mCursor.moveToNext());
        }
        return movieArray;
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content),
                        R.string.retry_text, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.hit_retry, new SnackListener());
        snackbar.show();
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    @SuppressLint("StaticFieldLeak")
    class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        final Context mContext;
        final RecyclerView mActivitiesRecyclerView;


        public FetchMovieTask(Context context,
                              RecyclerView recyclerView) {
            mContext = context;
            mActivitiesRecyclerView = recyclerView;


        }

        @Override
        protected String[] doInBackground(String... params) {
            Boolean isConnected = CheckNetworkConnection.checkConnection(MainActivity.this);
            String[] movieParsed = new String[2];


            if (params[0].equals("initial")) {
                stateOfActivity = "popular";

                if (!isConnected) {
                    cancel(true);
                }

                try {
                    String jsonDataPopular = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.buildPopularUri());
                    movieParsed[0] = jsonDataPopular;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String jsonDataTopRated = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.buildRatedUri());
                    movieParsed[1] = jsonDataTopRated;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMoviesArray = movieParsed;

            }


            if (params[0].equals("popular")) {
                if (!isConnected) {
                    cancel(true);
                }

                stateOfActivity = params[0];
                movieParsed = mMoviesArray;
                return movieParsed;
            }

            if (params[0].equals("top_rated")) {
                if (!isConnected) {
                    cancel(true);
                }
                stateOfActivity = params[0];
                movieParsed = mMoviesArray;
                return movieParsed;

            }

            if (params[0].equals("favorites")) {
                if (!isConnected) {
                    cancel(true);
                }
                favoriteState = true;
                movieParsed = null;
            }


            return movieParsed;
        }


        @Override
        protected void onPostExecute(String[] s) {

            if (s != null) {
                String popular = s[0];
                String rated = s[1];


                ArrayList<MovieInfoDetailObject> movieArrayPopular = new ArrayList<>();
                ArrayList<MovieInfoDetailObject> movieArrayRated = new ArrayList<>();
                ArrayList<MovieInfoDetailObject> completeListMovies = new ArrayList<>();


                try {
                    movieArrayPopular = JsonUtils.parseJsonObject(popular, "popular");
                    movieArrayRated = JsonUtils.parseJsonObject(rated, "top_rated");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                completeListMovies.addAll(movieArrayPopular);
                completeListMovies.addAll(movieArrayRated);

                mCurrentMovies = completeListMovies;

                ArrayList<MovieInfoDetailObject> listMoviesTypes = getListOfMovies(stateOfActivity);
                int arrayListSize = listMoviesTypes.size();


                //Get the view from the Context object
                GridViewAdapter adapter = new GridViewAdapter(arrayListSize, listMoviesTypes, MainActivity.this);
                mActivitiesRecyclerView.setAdapter(adapter);


            } else if (s == null) {
                if (checkCursorData()) {
                    ArrayList<MovieInfoDetailObject> movieArray = convertCursorToMovienfoHolder();
                    int arrayListSize = movieArray.size();

                    GridViewAdapter adapter = new GridViewAdapter(arrayListSize, movieArray, MainActivity.this);
                    mActivitiesRecyclerView.setAdapter(adapter);
                } else if (!checkCursorData()) {
                    favoriteState = false;
                    task.cancel(true);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (isCancelled()) {
                showSnackBar();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!checkCursorData() && favoriteState) {
            favoriteState = false;
            task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute(stateOfActivity);
        }
        if (checkCursorData() && favoriteState) {
            task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute("favorites");
        }

    }//Loader used to check for network connection.

    private final LoaderManager.LoaderCallbacks checkConnection =
            new LoaderManager.LoaderCallbacks<Boolean>() {
                @Override
                public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                    return new CheckConnectionLoader(MainActivity.this);
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }
            };

    //Object to use as the snackbar's listener.
    public class SnackListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            task = new FetchMovieTask(mContext, mRecyclerView);
            stateOfActivity = "";
            callNetwork();

        }
    }
}


