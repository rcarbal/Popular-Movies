package com.example.rcarb.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rcarb.popularmovies.Data.Contract;
import com.example.rcarb.popularmovies.Data.FavoriteMovieDbHelper;
import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.MovieInfoHolder;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;

import org.json.JSONException;

import java.io.IOException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements GridViewAdapter.OnItemClicked {

    private static final String DB_FULL_PATH = "/data/data/com.example.rcarb.popularmovies/databases/FavoriteMovies.db";

    private RecyclerView mRecyclerView;
    private ArrayList<MovieInfoHolder> mCurrentMovies;
    private FetchMovieTask task;
    private Context mContext;
    private Cursor mCursor;
    private String[] mMoviesArray;

    //Database global variables
    private FavoriteMovieDbHelper mMovieDbHelper;
    private SQLiteDatabase mDatabse;

    private String stateOfActivity = "";
    private boolean favoriteState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkDataBaseExists()) {
            checkCursorData();

        }
        mContext = MainActivity.this;

        //initialize the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //The Grid will not change size
        mRecyclerView.setHasFixedSize(true);

        //Sets up the layout manager as a GridView.
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);

        //Attaches the adaptor
        mRecyclerView.setLayoutManager(layoutManager);


        task = new FetchMovieTask(mContext, mRecyclerView);

        //The Default param is to query for most popular.
        task.execute("initial");
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

        //depending on the item selected on the menu item, the proper query param will be used.

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


    //Checks to see if a contract already exists
    private boolean checkDataBaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database does not exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(this, "Databse does exist", Toast.LENGTH_SHORT).show();
        return true;
    }

    //Reads the databse of favorite movies
    private Cursor getAllFavoriteMovies() {
        mMovieDbHelper = new FavoriteMovieDbHelper(this);
        //Set the readable databse
        mDatabse = mMovieDbHelper.getReadableDatabase();
        return mDatabse.query(
                Contract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
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


    //Checks to see if the global cursor is holding any moviw info
    private boolean checkCursorData() {
        Cursor cursor = getAllFavoriteMovies();
        if (cursor != null && cursor.getCount() > 0) {
            mCursor = cursor;
            return true;
        }
        return false;
    }

    //Gets the list of movies depending on the stateOfActivity value
    private ArrayList<MovieInfoHolder> getListOfMovies(String typeOfMovie) {
        ArrayList<MovieInfoHolder> movies = new ArrayList<>();
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

        //Takes the clicked RecyclerView vies id and saves the selected movie information
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        //Pass throught the MovieHolder object
        MovieInfoHolder holder = mCurrentMovies.get(getIndexOfCurrentMovies(movieId));
        intent.putExtra("movieId", holder.getMovieId());
        intent.putExtra("moviePoster", holder.getMoviePoster());
        intent.putExtra("movieTitle", holder.getMovieTitle());
        intent.putExtra("movieRelease", holder.getMovieReleaseDate());
        intent.putExtra("movieLength", 0);
        intent.putExtra("movieRating", holder.getMovieRating());
        intent.putExtra("movieDescription", holder.getMovieDescription());
        long id = getMovieIdColumn(movieId);
        if (id == -1) {
            intent.putExtra("movieFavorite", holder.getFavorite());
            intent.putExtra("movieColumn", holder.getColumn());
            //intent.putExtra("movieFavorite", true);
            //intent.putExtra("movieColumn", id);
        } else {
            intent.putExtra("movieFavorite", true);
            intent.putExtra("movieColumn", id);
        }
        intent.putExtra("movieType", holder.getListDescrition());

//        if (getMovieId(holder.getMovieTitle())== -1){

//        }else{
//            intent.putExtra("movieFavorite", true);
//            intent.putExtra("movieColumn", getMovieId(holder.getMovieTitle()));
//        }


        startActivity(intent);
    }

    //Converts the current Cursor data into an arraylist.
    private ArrayList<MovieInfoHolder> convertCursorToMovienfoHolder() {
        int movieId;
        String moviePoster;
        String movieTitle;
        String movieReleaseDate;
        int movieLength;
        String movieRating;
        String movieDescription;
        boolean mFavorite;
        long databaseColumn;

        ArrayList<MovieInfoHolder> movieArray = new ArrayList<>();


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

                    //Add the movie values into MovieInfoHolder.
                    MovieInfoHolder movieInfo = new MovieInfoHolder();
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


    //Checks to see if arraylist object has data.
    private boolean checkMovieArrays(ArrayList<MovieInfoHolder> movieArrays) {
        if (movieArrays.size() < 0) {
            return true;
        }
        return false;
    }

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

            String[] movieParsed = new String[2];


            if (params[0].equals("initial")) {
                stateOfActivity = "popular";

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

                stateOfActivity = params[0];
                movieParsed = mMoviesArray;
                return movieParsed;
            }

            if (params[0].equals("top_rated")) {
                stateOfActivity = params[0];
                movieParsed = mMoviesArray;
                return movieParsed;

            }

            if (params[0].equals("favorites")) {
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


                ArrayList<MovieInfoHolder> movieArrayPopular = new ArrayList<>();
                ArrayList<MovieInfoHolder> movieArrayRated = new ArrayList<>();
                ArrayList<MovieInfoHolder> completeListMovies = new ArrayList<>();


                try {
                    movieArrayPopular = JsonUtils.parseJsonObject(popular, "popular");
                    movieArrayRated = JsonUtils.parseJsonObject(rated, "top_rated");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                completeListMovies.addAll(movieArrayPopular);
                completeListMovies.addAll(movieArrayRated);

                mCurrentMovies = completeListMovies;

                ArrayList<MovieInfoHolder> listMoviesTypes = getListOfMovies(stateOfActivity);
                int arrayListSize = listMoviesTypes.size();


                //Get the view from the Context object
                GridViewAdapter adapter = new GridViewAdapter(arrayListSize, listMoviesTypes, MainActivity.this);
                mActivitiesRecyclerView.setAdapter(adapter);


            } else if (s == null) {
                if (checkCursorData()) {
                    ArrayList<MovieInfoHolder> movieArray = convertCursorToMovienfoHolder();
                    int arrayListSize = movieArray.size();

                    GridViewAdapter adapter = new GridViewAdapter(arrayListSize, movieArray, MainActivity.this);
                    mActivitiesRecyclerView.setAdapter(adapter);
                } else if (!checkCursorData()) {
                    favoriteState = false;
                    cancel(true);
                }
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

        //if favorites list was displayed then all the movies were removed from favorites
        if (!checkCursorData() && favoriteState) {
            favoriteState = false;
            task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute(stateOfActivity);
        }

        //if the cursor is not null
        if (checkCursorData() && favoriteState) {
            task = new FetchMovieTask(mContext, mRecyclerView);
            task.execute("favorites");
        }

    }
}


