package com.example.rcarb.popularmovies;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rcarb.popularmovies.Data.Contract;
import com.example.rcarb.popularmovies.Data.GetMovieDetailLoader;
import com.example.rcarb.popularmovies.Utils.CheckNetworkConnection;
import com.example.rcarb.popularmovies.Utils.IntentConstants;
import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.MovieInfoHolder;
import com.example.rcarb.popularmovies.Utils.MovieReviewAsyncLoader;
import com.example.rcarb.popularmovies.Utils.MovieReviewObject;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Utils.TrailerInfoHolder;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class DetailActivity extends AppCompatActivity implements
        MovieTrailerAdaptor.TrailerOnClickListener {


    private MovieInfoHolder mMovieInfoHolder;
    private ArrayList<TrailerInfoHolder> mActivityTrailer;
    @SuppressWarnings("FieldCanBeLocal")
    private GetMovieInfoTask task;
    private int currentMovieId;
    private boolean mInstanceStateLoaded = false;

    private Parcelable mTrailerRecyclerViewState;
    private Parcelable mReviewRecyclerViewState;


    private LinearLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private ScrollView mScrollView;

    private static final int GET_MOVIE_LENGTH = 1;
    private static final int GET_MOVIE_REVIEWS = 1;

    private boolean mIsRestored = false;
    private int mTrailerScrollPosition = -1;
    private int mReviewScrollPosition = -1;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTrailerRecyclerViewState = mTrailerRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(IntentConstants.MOVIE_TRAILER_LAYOUT_MANGER, mTrailerRecyclerViewState);

        mReviewRecyclerViewState = mReviewsRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(IntentConstants.MOVIE_REVIEW_LAYOUT_MANAGER, mReviewRecyclerViewState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
        setContentView(R.layout.test_layout);


        mTrailerRecyclerViewState = new Bundle();
        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mTrailerLayoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);

        mReviewRecyclerViewState = new Bundle();
        mReviewsRecyclerView = findViewById(R.id.reviews_rv);
        mReviewLayoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mReviewsRecyclerView.setLayoutManager(mReviewLayoutManager);





        getActivityIntent();
//        getMovieReviewAndLength();
//        loadMovieReviews();
    }

    private void loadMovieReviews() {
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

    //Get movie trailers
    private void getMovieTrailers() {

        task = new GetMovieInfoTask();
        task.execute(currentMovieId);

    }

    private void setupTrailerRecyclerView() {

        if (mActivityTrailer.size() > 0) {

            List<TrailerInfoHolder> list = mActivityTrailer;
            mTrailerRecyclerView.hasFixedSize();
            RecyclerView.Adapter mAdaptor = new MovieTrailerAdaptor(list,
                    this);

            mTrailerRecyclerView.setAdapter(mAdaptor);
            loadMovieReviews();

        }
    }


    //Set the AsyncTask Activity


    //Gets the intent extras and sets up a MovieInfoHolder object.
    private void getActivityIntent() {
        if (!mIsRestored){
            Intent intent = getIntent();
            if (!mInstanceStateLoaded) {
                mMovieInfoHolder = new MovieInfoHolder();
            }
            mMovieInfoHolder = intent.getParcelableExtra("movie");
        }
        setupLayout();
    }

    @SuppressLint("SetTextI18n")
    private void setupLayout() {
//        //Set Movie Poster
//        ImageView posterImage = findViewById(R.id.poster_image);
//        Picasso.with(DetailActivity.this).load(UriBuilderUtil.imageDownload(mMovieInfoHolder.getMoviePoster()))
//                .into(posterImage);
//        //Set Movie Title
//        TextView movieTitle = findViewById(R.id.movie_title);
//        movieTitle.setText(mMovieInfoHolder.getMovieTitle());
//
//        //Set Release Date
//        TextView movieReleaseDate = findViewById(R.id.release_date);
//        String dateReleased = mMovieInfoHolder.getMovieReleaseDate();
//        String extractYear = dateReleased.substring(0, 4);
//        movieReleaseDate.setText(extractYear);
//
//        //Set movie rating
//        TextView movieRating = findViewById(R.id.user_rating);
//        movieRating.setText(mMovieInfoHolder.getMovieRating() + "/10");
//        movieRating.setTypeface(null, Typeface.BOLD_ITALIC);
//
//        //Set Movie Description
//        TextView movieDescription = findViewById(R.id.movieDescription);
//        movieDescription.setText(mMovieInfoHolder.getMovieDescription());
//
//        //Sets ImageViews' onClick attributes
//        final ImageView favorite = findViewById(R.id.favorite);
//        favorite.setClickable(true);
//        final ImageView notFavorite = findViewById(R.id.not_favorite);
//        notFavorite.setClickable(true);
//
//
//        if (mMovieInfoHolder.getFavorite()) {
//            favorite.setVisibility(View.VISIBLE);
//            notFavorite.setVisibility(View.GONE);
//        }
//        if (!mMovieInfoHolder.getFavorite()) {
//            favorite.setVisibility(View.GONE);
//            notFavorite.setVisibility(View.VISIBLE);
//        }
//
//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMovieInfoHolder.setFavorite(false);
//                removeFromFavorite(mMovieInfoHolder.getColumn());
//                favorite.setVisibility(View.GONE);
//                notFavorite.setVisibility(View.VISIBLE);
//                Toast.makeText(DetailActivity.this, "\"" + mMovieInfoHolder.getMovieTitle() + "\"" +
//                        " was removed from favorites", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//
//        notFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMovieInfoHolder.setFavorite(true);
//                setAsFavorite();
//                favorite.setVisibility(View.VISIBLE);
//                notFavorite.setVisibility(View.GONE);
//                Toast.makeText(DetailActivity.this, "\"" + mMovieInfoHolder.getMovieTitle() + "\"" +
//                        " was set from favorites", Toast.LENGTH_SHORT).show();
//            }
//        });

        currentMovieId = mMovieInfoHolder.getMovieId();
        getMovieTrailers();
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
        getContentResolver().delete(uri, null, null);
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content),
                        R.string.retry_text, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.hit_retry, new SnackListener());
        snackbar.show();
    }


    @Override
    public void onClickTrailer(String movieKey) {
        String trailerKey = movieKey;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(UriBuilderUtil.buildTrailerUri(trailerKey));
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    class GetMovieInfoTask extends AsyncTask<Integer, Void, Void> {

        ArrayList<TrailerInfoHolder> trailersForMovies = new ArrayList<>();

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
            mActivityTrailer = trailersForMovies;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setupTrailerRecyclerView();
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
                    TextView textView = findViewById(R.id.movie_length);
                    textView.setText(getString(R.string.minutes_text, data));
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

                    mReviewsRecyclerView.hasFixedSize();


                    int size = data != null ? data.size() : 0;

                    RecyclerView.Adapter mAdaptor = new MovieReviewsAdaptor(size, data);
                    mReviewsRecyclerView.setAdapter(mAdaptor);
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<MovieReviewObject>> loader) {
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        if (mReviewRecyclerViewState != null){
            mReviewsRecyclerView.getLayoutManager().onRestoreInstanceState(mReviewRecyclerViewState);
        }
        if (mTrailerRecyclerViewState != null){
            mTrailerRecyclerView.getLayoutManager().onRestoreInstanceState(mTrailerRecyclerViewState);
        }
    }
}
