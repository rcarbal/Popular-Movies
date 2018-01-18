package com.example.rcarb.popularmovies.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rcarb.popularmovies.DetailActivity;
import com.example.rcarb.popularmovies.MovieReviewsAdaptor;
import com.example.rcarb.popularmovies.R;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "StatementWithEmptyBody"})
public class MovieReviewsTask extends AsyncTask<Integer, Void, String> {

    private final WeakReference<Activity> mWeakActivity;

    public MovieReviewsTask(DetailActivity activity){
       mWeakActivity = new WeakReference<Activity>(activity);



  }

    @Override
    protected String doInBackground(Integer... movieId) {

        int movieIdToParse = movieId[0];
        String movieReviews = "";
        try {
            movieReviews = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.parseMovieReviews(movieIdToParse));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieReviews;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Activity activity = mWeakActivity.get();

        if (activity!=null){

        }
        ArrayList<MovieReviewObject> movieArrayOfMovieReviews = null;
        try {
            movieArrayOfMovieReviews = JsonUtils.parseReviewsForMovie(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView mRecyclerView = activity.findViewById(R.id.reviews_rv);
        mRecyclerView.hasFixedSize();

        //LinearLayout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int size = movieArrayOfMovieReviews != null ? movieArrayOfMovieReviews.size() : 0;

        RecyclerView.Adapter mAdaptor = new MovieReviewsAdaptor(size, movieArrayOfMovieReviews);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdaptor);

    }
}