package com.example.rcarb.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.popularmovies.ComplexMovieAdaptor;
import com.example.rcarb.popularmovies.R;

/**
 * Created by rcarb on 3/29/2018.
 */

public class MovieInfoDetailHolder extends RecyclerView.ViewHolder{

    public ImageView mPoster, mFavorite, mNotFavorite;
    private TextView mMovieTitle, mMovieReleaseDate,mMovieLength, mMovieRating, mMovieDescription;

    public MovieInfoDetailHolder(View itemView, final ComplexMovieAdaptor.OnItemClicked onItemClicked) {
        super(itemView);
        mPoster = itemView.findViewById(R.id.poster_image);
        mFavorite = itemView.findViewById(R.id.favorite);
        mNotFavorite = itemView.findViewById(R.id.not_favorite);
        mMovieTitle = itemView.findViewById(R.id.movie_title);
        mMovieReleaseDate = itemView.findViewById(R.id.release_date);
        mMovieLength = itemView.findViewById(R.id.movie_length);
        mMovieRating = itemView.findViewById(R.id.user_rating);
        mMovieDescription = itemView.findViewById(R.id.movieDescription);
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.onItemClick("", true);
            }
        });
        mNotFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.onItemClick("", false);
            }
        });
    }

    public void setPoster(ImageView poster){
        mPoster = poster;
    }
    public ImageView getPoster(){
        return mPoster;
    }
    public void setFavorite(ImageView favorite){
        mFavorite = favorite;
    }
    public ImageView getFavorite(){
        return mFavorite;
    }
    public void setNotFavorite(ImageView notFavorite){
        mNotFavorite = notFavorite;
    }
    public ImageView getNotFavorite(){
        return mNotFavorite;
    }
    public void setMovieTitle(TextView movieTitle){
        mMovieTitle = movieTitle;
    }
    public TextView getMovieTitle(){
        return mMovieTitle;
    }
    public void setMovieReleaseDate(TextView releaseDate){
        mMovieReleaseDate = releaseDate;
    }
    public TextView getMovieReleaseDate(){
        return mMovieReleaseDate;
    }
    public void setMovieLength(TextView length){
        mMovieLength = length;
    }
    public TextView getMovieLength(){
        return mMovieLength;
    }
    public void setMovieRating(TextView rating){
        mMovieRating = rating;
    }
    public TextView getMovieRating(){
        return mMovieRating;
    }

    public void setMovieDescription(TextView descrition){
        mMovieDescription = descrition;
    }
    public TextView getMovieDescription(){
        return mMovieDescription;
    }

}
