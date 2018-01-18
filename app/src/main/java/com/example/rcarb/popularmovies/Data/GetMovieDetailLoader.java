package com.example.rcarb.popularmovies.Data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rcarb.popularmovies.Utils.JsonUtils;
import com.example.rcarb.popularmovies.Utils.NetWorkUtils;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;

import org.json.JSONException;

import java.io.IOException;

public class GetMovieDetailLoader extends AsyncTaskLoader<String> {

    private final int mMovieId;
    public GetMovieDetailLoader(Context context,
                                int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        int movieLength = 0;
        try {
            String jsonDataPopular = NetWorkUtils.getResponseFromHttpUrl(UriBuilderUtil.parseMovieDetail(mMovieId));
             movieLength = JsonUtils.parseMovieDetails(jsonDataPopular);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(movieLength);
    }
}
