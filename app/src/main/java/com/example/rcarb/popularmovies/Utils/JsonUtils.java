package com.example.rcarb.popularmovies.Utils;

import com.example.rcarb.popularmovies.Objects.MovieInfoDetailObject;
import com.example.rcarb.popularmovies.Objects.MovieReviewObject;
import com.example.rcarb.popularmovies.Objects.TrailerInfoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonUtils {

    //Parses the movieArray object that haolds all the movies for popular or top rated parse.

    public static ArrayList<MovieInfoDetailObject> parseJsonObject(String jsonDataString, String listType) throws JSONException {

        JSONObject jsonData = new JSONObject(jsonDataString);


        JSONArray resultsInData = jsonData.getJSONArray("results");

        //StringArray to hold the movie poster
        ArrayList<MovieInfoDetailObject> movieArray = new ArrayList<>();
        for (int i = 0; i < resultsInData.length(); i++) {

            JSONObject object = resultsInData.getJSONObject(i);
            if (object != null) {
                int movieId = object.getInt("id");
                String moviePoster = (String) object.get("poster_path");
                String movieTitle = (String) object.get("title");
                String releaseDate = (String) object.get("release_date");

                String movieRating = object.get("vote_average").toString();
                String movieDescription = (String) object.get("overview");

                //Add the movie values into MovieInfoDetailObject.
                MovieInfoDetailObject movieInfo = new MovieInfoDetailObject();
                movieInfo.setMovieId(movieId);
                movieInfo.setMoviePoster(moviePoster);
                movieInfo.setMovieTitle(movieTitle);
                movieInfo.setMovieReleaseDate(releaseDate);

                movieInfo.setMovieRating(movieRating);
                movieInfo.setMovieDescription(movieDescription);
                movieInfo.setFavorite(false);
                movieInfo.setColumn(0);
                movieInfo.setListDescription(listType);

                movieInfo.setMovieLength("");
                movieArray.add(i, movieInfo);

            }
        }
        return movieArray;
    }

    //Creates the array object for the  trailers of specified movie.

    public static ArrayList<TrailerInfoObject> parseJsonTrailerObject(String jsonDataString)throws JSONException{
        JSONObject jsondata = new JSONObject(jsonDataString);

        JSONArray resultsInData = jsondata.getJSONArray("results");

        //ArrayList that will hold the movie trailer keys
        ArrayList<TrailerInfoObject> trailerArray =  new ArrayList<>();
        for (int i = 0; i < resultsInData.length(); i++){
            JSONObject object = resultsInData.getJSONObject(i);
            if (object != null){

                //Extract the movie key
                String movieKey = object.getString("key");
                String trailerName = object.getString("name");

                //add the movie key to the array object
                TrailerInfoObject trailerKeyHolder = new TrailerInfoObject();
                trailerKeyHolder.setTrailerKey(movieKey);
                trailerKeyHolder.setTrailerName(trailerName);
                trailerArray.add(trailerKeyHolder);

            }
        }
        return trailerArray;
    }

    //Returns an array for the reviews of a specified movie.

    public static ArrayList<MovieReviewObject> parseReviewsForMovie(String movieIdReviews) throws JSONException {

        JSONObject jsonData = new JSONObject(movieIdReviews);

        JSONArray resultsInJsonObject = jsonData.getJSONArray("results");

        ArrayList<MovieReviewObject> arrayListOfReviews = new ArrayList<>();
        for (int i = 0; i <resultsInJsonObject.length(); i++){
            JSONObject object = resultsInJsonObject.getJSONObject(i);
            if (object != null){

                //Extract the reviews
                String name = object.getString("author");
                String review = object.getString("content");

                //set the extracted information to a ReviewObhect
                MovieReviewObject reviewObject = new MovieReviewObject();
                reviewObject.setAuthorName(name);
                reviewObject.setReview(review);

                arrayListOfReviews.add(reviewObject);
            }
        }
        return arrayListOfReviews;
    }

    //Parses for movie deatils
    public static int parseMovieDetails(String jsonString) throws JSONException{

        JSONObject jsonData = new JSONObject(jsonString);
        @SuppressWarnings("UnnecessaryLocalVariable")
        int movieLength = jsonData.getInt("runtime");

        return movieLength;
    }

}
