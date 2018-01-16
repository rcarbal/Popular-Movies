package com.example.rcarb.popularmovies.Utils;

import android.net.Uri;

import com.example.rcarb.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rcarb on 10/18/2017.
 * Java class that builds the Uri, to query a list of movies, or single movie information.
 */

public class UriBuilderUtil {


    private static final String mQueryWithAPIKey = "api_key";
    private final static String BASE_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";
    private final static String BASE_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private final static String BASE_TRAILER_URL ="http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_VIDEO = "v";

    private final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?";

    //variables for parsing image
    private static final String BASE_URL_FOR_IMAGE_PARSING = "http://image.tmdb.org/t/p/w185/";

    //Parses the popular movies list
    public static URL buildPopularUri() {

        Uri builtUri =
                Uri.parse(BASE_POPULAR_URL).buildUpon()
                        .appendQueryParameter(mQueryWithAPIKey, API_KEY)
                        .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Parses the top rated movie list
    public static URL buildRatedUri() {

        Uri builtUri =
                Uri.parse(BASE_RATED_URL).buildUpon()
                        .appendQueryParameter(mQueryWithAPIKey, API_KEY)
                        .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String imageDownload(String image) {
        return BASE_URL_FOR_IMAGE_PARSING + image;
    }

//     This section is for the parsing of movie trailers and reviews.



    //Parses the movie Trailers for the detailview
    public static URL buildParsingTrailerUri(int movieId){

        Uri builtUri =
                Uri.parse(BASE_TRAILER_URL).buildUpon()
                        .appendPath(String.valueOf(movieId))
                        .appendPath("videos")
                        .appendQueryParameter(mQueryWithAPIKey, API_KEY)
                        .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Uri used for opening the intent for youtube trailer.

    public static Uri buildTrailerUri(String trailerKey){
        Uri builtUri =
                Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendQueryParameter(BASE_VIDEO, trailerKey)
                .build();
        return builtUri;
    }

    //Uri for parsing the reviews from the API for the specified movie.
    public static URL parseMovieReviews(int movieId){
        Uri builtUri =
                Uri.parse(BASE_TRAILER_URL).buildUpon()
                        .appendPath(String.valueOf(movieId))
                        .appendPath("reviews")
                        .appendQueryParameter(mQueryWithAPIKey, API_KEY)
                        .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



}
