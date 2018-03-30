package com.example.rcarb.popularmovies.Utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rcarb.popularmovies.Objects.MovieReviewObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rcarb on 3/26/2018.
 */

public class MovieReviewAsyncLoader extends AsyncTaskLoader {
    private int mMovieId;


    public MovieReviewAsyncLoader(Context context,
                                  int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<MovieReviewObject> loadInBackground() {
        String movieReviews = "";
        try {
            movieReviews = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.parseMovieReviews(mMovieId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<MovieReviewObject> movieArrayOfMovieReviews = null;
        try {
            movieArrayOfMovieReviews = JsonUtils.parseReviewsForMovie(movieReviews);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieArrayOfMovieReviews;
    }
}
