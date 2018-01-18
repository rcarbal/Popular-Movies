package com.example.rcarb.popularmovies;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rcarb.popularmovies.Data.Contract;
import com.example.rcarb.popularmovies.Data.GetMovieDetailLoader;
import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.MovieInfoHolder;
import com.example.rcarb.popularmovies.Utils.MovieReviewsTask;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Utils.TrailerInfoHolder;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class DetailActivity extends AppCompatActivity {

    private MovieInfoHolder movieInfoHolder;
    private ArrayList<TrailerInfoHolder> mActivityTrailer;
    @SuppressWarnings("FieldCanBeLocal")
    private GetMovieInfoTask task;

    //Layouts for the trailers
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;

    private View view1;
    private View view2;
    private View view3;

    private int mNumberOfTrailers;
    private boolean instanceStateLoaded = false;

    private static final int GET_MOVIE_LENGTH = 1;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("FAVORITE_STATE", movieInfoHolder.getFavorite());
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            movieInfoHolder = new MovieInfoHolder();
            movieInfoHolder.setFavorite(savedInstanceState.getBoolean("FAVORITE_STATE"));
            instanceStateLoaded = true;
        }
        setContentView(R.layout.activity_detail);
        getActivityIntent();
        setupLayout();

        int currentMovieId= movieInfoHolder.getMovieId();
        task = new GetMovieInfoTask();
        task.execute(currentMovieId);

        MovieReviewsTask reviewTask = new MovieReviewsTask(this);
        reviewTask.execute(movieInfoHolder.getMovieId());
        getMovieLength();

    }

    private void getMovieLength() {
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(GET_MOVIE_LENGTH);
        if (loader == null){
            loaderManager.initLoader(GET_MOVIE_LENGTH, null, movieLengthLoader);
        }else{
            loaderManager.restartLoader(GET_MOVIE_LENGTH, null, movieLengthLoader);
        }
    }

    //Set the AsyncTask Activity



    //Gets the intent extras and sets up a MovieInfoHolder object.
    private void getActivityIntent() {
        Intent intent = getIntent();
        if (!instanceStateLoaded) {
            movieInfoHolder = new MovieInfoHolder();
        }
        movieInfoHolder = intent.getParcelableExtra("movie");
        setupLayout();
    }

    @SuppressLint("SetTextI18n")
    private void setupLayout() {
        //Set Movie Poster
        ImageView posterImage = findViewById(R.id.poster_image);
        Picasso.with(DetailActivity.this).load(UriBuilderUtil.imageDownload(movieInfoHolder.getMoviePoster()))
                .into(posterImage);
        //Set Movie Title
        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(movieInfoHolder.getMovieTitle());

        //Set Release Date
        TextView movieReleaseDate = findViewById(R.id.release_date);
        String dateReleased = movieInfoHolder.getMovieReleaseDate();
        String extractYear = dateReleased.substring(0, 4);
        movieReleaseDate.setText(extractYear);

        //Set movie rating
        TextView movieRating = findViewById(R.id.user_rating);
        movieRating.setText(movieInfoHolder.getMovieRating() + "/10");
        movieRating.setTypeface(null, Typeface.BOLD_ITALIC);

        //Set Movie Description
        TextView movieDescription = findViewById(R.id.movieDescription);
        movieDescription.setText(movieInfoHolder.getMovieDescription());

        //Sets ImageViews' onClick attributes
        final ImageView favorite = findViewById(R.id.favorite);
        favorite.setClickable(true);
        final ImageView notFavorite = findViewById(R.id.not_favorite);
        notFavorite.setClickable(true);
        //initiate the LinearLayouts
        layout1 = findViewById(R.id.trailer1_layout);
        layout1.setVisibility(View.GONE);
        view1 = findViewById(R.id.view1);
        view1.setVisibility(View.GONE);

        layout2 = findViewById(R.id.trailer2_layout);
        layout2.setVisibility(View.GONE);
        view2 = findViewById(R.id.view2);
        view2.setVisibility(View.GONE);

        layout3 = findViewById(R.id.trailer3_layout);
        layout3.setVisibility(View.GONE);
        view3 = findViewById(R.id.view3);
        view3.setVisibility(View.GONE);

        //set the click for the play button
        ImageView play1 = findViewById(R.id.trailer1_button);
        play1.setClickable(true);
        ImageView play2 = findViewById(R.id.trailer2_button);
        play2.setClickable(true);
        ImageView play3 = findViewById(R.id.trailer3_button);
        play3.setClickable(true);


        if (movieInfoHolder.getFavorite()) {
            favorite.setVisibility(View.VISIBLE);
            notFavorite.setVisibility(View.GONE);
        }
        if (!movieInfoHolder.getFavorite()) {
            favorite.setVisibility(View.GONE);
            notFavorite.setVisibility(View.VISIBLE);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieInfoHolder.setFavorite(false);
                removeFromFavorite(movieInfoHolder.getColumn());
                favorite.setVisibility(View.GONE);
                notFavorite.setVisibility(View.VISIBLE);
                Toast.makeText(DetailActivity.this, "\"" + movieInfoHolder.getMovieTitle() + "\"" +
                        " was removed from favorites", Toast.LENGTH_SHORT).show();


            }
        });

        notFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieInfoHolder.setFavorite(true);
                setAsFavorite();
                favorite.setVisibility(View.VISIBLE);
                notFavorite.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this, "\"" + movieInfoHolder.getMovieTitle() + "\"" +
                        " was set from favorites", Toast.LENGTH_SHORT).show();
            }
        });

        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailerKey = mActivityTrailer.get(0).getTrailerKey();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(UriBuilderUtil.buildTrailerUri(trailerKey));
                startActivity(intent);

            }
        });
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailerKey = mActivityTrailer.get(1).getTrailerKey();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(UriBuilderUtil.buildTrailerUri(trailerKey));
                startActivity(intent);

            }
        });
        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailerKey = mActivityTrailer.get(2).getTrailerKey();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(UriBuilderUtil.buildTrailerUri(trailerKey));
                startActivity(intent);

            }
        });

    }

    /**
     * Save's the current movie details that are being displayed into the FavoriteMovies.db file.
     */
    private void setAsFavorite() {
        //The Content that goes into the databse.
        ContentValues values = new ContentValues();
        values.put(Contract.MovieEntry.COLUMN_MOVIE_TITLE, movieInfoHolder.getMovieTitle());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_ID, movieInfoHolder.getMovieId());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_POSTER, movieInfoHolder.getMoviePoster());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_YEAR, movieInfoHolder.getMovieReleaseDate());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_RATING, movieInfoHolder.getMovieRating());
        values.put(Contract.MovieEntry.COLUMN_MOVIE_DESCRIPTION, movieInfoHolder.getMovieDescription());

        getContentResolver().insert(Contract.MovieEntry.BASE_CONTENT_URI_FAVORITES,values);
    }

    //Sets the TrailerLayout
    private void setTrailerLayout(){
        if (mNumberOfTrailers > 0){
            for (int i = 0; i <= mNumberOfTrailers; i++){
                if (i == 1){
                    layout1.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                }
                else if (i == 2){
                    layout2.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }
                else if (i == 3){
                    layout3.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);
                }

            }
        }
    }


//    Sets the number of trailers to the gloaba varaible.
    private void setNumberOfTrailers(){
        if (mActivityTrailer.size() <= 3 && mActivityTrailer.size() > 0) {
            mNumberOfTrailers = mActivityTrailer.size();
        } else if (mActivityTrailer.size() > 3) {
            mNumberOfTrailers = 3;
        }
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

    @SuppressLint("StaticFieldLeak")
    class GetMovieInfoTask extends AsyncTask<Integer, Void, String>{

        ArrayList<TrailerInfoHolder> trailersForMovies = new ArrayList<>();

        @Override
        protected String doInBackground(Integer... integers) {
            String jsonDataTrailers = "";
            try {
                jsonDataTrailers = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.buildParsingTrailerUri(integers[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonDataTrailers;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            @SuppressWarnings("UnnecessaryLocalVariable")
            String trailerJsonString = s;

            try {
                trailersForMovies = JsonUtils.parseJsonTrailerObject(trailerJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mActivityTrailer = trailersForMovies;
            setNumberOfTrailers();
            setTrailerLayout();
        }
    }
    //Loader to get the movie length.
    android.support.v4.app.LoaderManager.LoaderCallbacks<String> movieLengthLoader =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new GetMovieDetailLoader(DetailActivity.this, movieInfoHolder.getMovieId());
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

}
