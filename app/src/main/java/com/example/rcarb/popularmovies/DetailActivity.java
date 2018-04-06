package com.example.rcarb.popularmovies;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.rcarb.popularmovies.Data.Contract;
import com.example.rcarb.popularmovies.Data.GetMovieDetailLoader;
import com.example.rcarb.popularmovies.Objects.MovieInfoDetailObject;
import com.example.rcarb.popularmovies.Utils.CheckNetworkConnection;
import com.example.rcarb.popularmovies.Utils.IntentConstants;
import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.MovieReviewAsyncLoader;
import com.example.rcarb.popularmovies.Objects.MovieReviewObject;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Objects.TrailerInfoObject;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class DetailActivity extends AppCompatActivity implements ComplexMovieAdaptor.OnItemClicked  {


    private MovieInfoDetailObject mMovieInfoHolder;
    private ArrayList<TrailerInfoObject> mMovieTrailer;
    private ArrayList<MovieReviewObject> mMovieReviews;
    private ArrayList<Object> mConcateData;
    private ComplexMovieAdaptor mAdaptor;
    @SuppressWarnings("FieldCanBeLocal")
    private GetMovieInfoTask task;
    private int currentMovieId;
    private boolean mInstanceStateLoaded = false;


    private Parcelable mMovieDetailLayoutManagerState;
    private LinearLayoutManager mMovieDetailLayoutManager;
    private RecyclerView mMovieDetailRecyclerView;

    private static final int GET_MOVIE_LENGTH = 1;
    private static final int GET_MOVIE_REVIEWS = 1;

    private boolean mIsRestored = false;
    private boolean mIsFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);


        mMovieDetailLayoutManagerState = new Bundle();
        mMovieDetailLayoutManager = new LinearLayoutManager(this);
        mMovieDetailRecyclerView = findViewById(R.id.setup_detail_rv);
        mMovieDetailRecyclerView.setLayoutManager(mMovieDetailLayoutManager);


        getActivityIntent();
    }

    private void setupAdaptorsData() {
        mConcateData = new ArrayList<>();

        if (mMovieInfoHolder != null) {
            mConcateData.add(mMovieInfoHolder);
        }
        if (mMovieTrailer != null) {
            if (mMovieTrailer.size() > 0){
                mConcateData.addAll(mMovieTrailer);
            }
        }
        mConcateData.add("hello");
        if (mMovieReviews != null) {
            if (mMovieReviews.size() >0){
                mConcateData.addAll(mMovieReviews);
            }
        }
        mAdaptor = new ComplexMovieAdaptor(mConcateData, this);
        bindData();
    }

    private void bindData() {
        mMovieDetailRecyclerView.setAdapter(mAdaptor);
    }

    private void getMovieReviews() {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.ARGS_GET_MOVIE_ID, mMovieInfoHolder.getMovieId());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<MovieReviewObject>> getReviewLoader = loaderManager.getLoader(GET_MOVIE_REVIEWS);
        if (getReviewLoader == null) {
            loaderManager.initLoader(GET_MOVIE_REVIEWS, bundle, movieReview);
        } else {
            loaderManager.restartLoader(GET_MOVIE_REVIEWS, bundle, movieReview);
        }

    }

    private void getMovieReviewAndLength() {
        if (!CheckNetworkConnection.checkConnection(this)) {
            showSnackBar();
        } else {
            getMovieLength();
        }
    }

    private void getMovieLength() {
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(GET_MOVIE_LENGTH);
        if (loader == null) {
            loaderManager.initLoader(GET_MOVIE_LENGTH, null, movieLengthLoader);
        } else {
            loaderManager.restartLoader(GET_MOVIE_LENGTH, null, movieLengthLoader);
        }
    }

    private void getMovieTrailers() {

        task = new GetMovieInfoTask();
        task.execute(currentMovieId);

    }


    //Set the AsyncTask Activity
    //Gets the intent extras and sets up a MovieInfoDetailObject object.
    private void getActivityIntent() {
        if (!mIsRestored) {
            Intent intent = getIntent();
            if (!mInstanceStateLoaded) {
                mMovieInfoHolder = new MovieInfoDetailObject();
            }
            mMovieInfoHolder = intent.getParcelableExtra("movie");
        }
        setupLayout();
    }

    @SuppressLint("SetTextI18n")
    private void setupLayout() {

        if (mMovieInfoHolder != null) {
            mIsFavorite = mMovieInfoHolder.getFavorite();
            currentMovieId = mMovieInfoHolder.getMovieId();
        }
        getMovieLength();
    }

    /**
     * Save's the current movie details that are being displayed into the FavoriteMovies.db file.
     */
    private void setAsFavorite() {
        //The Content that goes into the databse.
        ContentValues values = new ContentValues();
        values.put(Contract.MovieEntry.COLUMN_MOVIE_TITLE, mMovieInfoHolder.getMovieTitle());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_ID, mMovieInfoHolder.getMovieId());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_POSTER, mMovieInfoHolder.getMoviePoster());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_YEAR, mMovieInfoHolder.getMovieReleaseDate());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_RATING, mMovieInfoHolder.getMovieRating());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_DESCRIPTION, mMovieInfoHolder.getMovieDescription());

        getContentResolver().insert(Contract.MovieEntry.BASE_CONTENT_URI_FAVORITES, values);
    }


    /**
     * Removes the current movie details from the database.
     */

    private void removeFromFavorite(long id) {
        //Build the uri for the given id.
        String stringId = Long.toString(id);
        Uri uri = Contract.MovieEntry.BASE_CONTENT_URI_FAVORITES;
        uri = uri.buildUpon().appendPath(stringId).build();
        int deleted = getContentResolver().delete(uri, null, null);
        if (deleted!= -1){
            Toast.makeText(DetailActivity.this, "\"" + mMovieInfoHolder.getMovieTitle() + "\"" +
                    " was removed from favorites", Toast.LENGTH_SHORT).show();
            mMovieInfoHolder.setFavorite(false);
            mIsFavorite = false;
            updateAdaptorData();
        }
    }

    private void updateAdaptorData() {
        mAdaptor.notifyDataSetChanged();
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content),
                        R.string.retry_text, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.hit_retry, new SnackListener());
        snackbar.show();
    }

    @Override
    public void onItemClick(String movieKey, boolean favoriteClicked) {
        if (!movieKey.equals("")){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(UriBuilderUtil.buildTrailerUri(movieKey));
            startActivity(intent);
        }else {
            if (favoriteClicked){
                changetFavoriteStatus();
        }else if (!favoriteClicked){
                changetFavoriteStatus();
            }
        }
    }

    private void changetFavoriteStatus() {
        if (!mIsFavorite){
            mMovieInfoHolder.setFavorite(true);
            setAsFavorite();
            Toast.makeText(DetailActivity.this, "\"" + mMovieInfoHolder.getMovieTitle() + "\"" +
                    " was set from favorites", Toast.LENGTH_SHORT).show();
            mMovieInfoHolder.setFavorite(true);
            mIsFavorite = true;
            updateAdaptorData();
        } else if(mIsFavorite){
            removeFromFavorite(mMovieInfoHolder.getColumn());
        }
    }


    @SuppressLint("StaticFieldLeak")
    class GetMovieInfoTask extends AsyncTask<Integer, Void, Void> {

        ArrayList<TrailerInfoObject> trailersForMovies = new ArrayList<>();

        @Override
        protected Void doInBackground(Integer... integers) {
            String jsonDataTrailers = "";
            try {
                jsonDataTrailers = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.buildParsingTrailerUri(integers[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                trailersForMovies = JsonUtils.parseJsonTrailerObject(jsonDataTrailers);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mMovieTrailer = trailersForMovies;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getMovieReviews();
        }
    }

    //Loader to get the movie length.
    android.support.v4.app.LoaderManager.LoaderCallbacks<String> movieLengthLoader =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new GetMovieDetailLoader(DetailActivity.this, mMovieInfoHolder.getMovieId());
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {
                    if (mMovieInfoHolder != null) {
                        mMovieInfoHolder.setMovieLength(data);
                        getMovieTrailers();
                    }
                }

                @Override
                public void onLoaderReset(Loader<String> loader) {
                }
            };


    //Object to use as the snackbar's listener.
    public class SnackListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            getMovieReviewAndLength();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //<---------------------------------------Loaders--------------------------------------------->
    private final LoaderManager.LoaderCallbacks<ArrayList<MovieReviewObject>> movieReview =
            new LoaderManager.LoaderCallbacks<ArrayList<MovieReviewObject>>() {
                @Override
                public Loader<ArrayList<MovieReviewObject>> onCreateLoader(int id, Bundle args) {
                    int idMovie = args.getInt(IntentConstants.ARGS_GET_MOVIE_ID, -1);
                    return new MovieReviewAsyncLoader(DetailActivity.this, idMovie);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<MovieReviewObject>> loader, ArrayList<MovieReviewObject> data) {
                    if (data.size() > 0) {
                        mMovieReviews = data;
                    }
                    setupAdaptorsData();
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<MovieReviewObject>> loader) {
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieDetailLayoutManagerState != null) {
            mMovieDetailRecyclerView.getLayoutManager().onRestoreInstanceState(mMovieDetailLayoutManagerState);
        }
    }
}
