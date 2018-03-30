package com.example.rcarb.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.popularmovies.Objects.MovieInfoDetailObject;
import com.example.rcarb.popularmovies.Objects.MovieReviewObject;
import com.example.rcarb.popularmovies.Objects.TrailerInfoObject;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;
import com.example.rcarb.popularmovies.ViewHolders.MovieInfoDetailHolder;
import com.example.rcarb.popularmovies.ViewHolders.MovieReviewHolder;
import com.example.rcarb.popularmovies.ViewHolders.MovieTrailerHolder;
import com.example.rcarb.popularmovies.ViewHolders.SimpleViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class ComplexMovieAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Object> mItems;
    private final OnItemClicked mItemClicked;

    public interface OnItemClicked {
        void onItemClicked(int integer);
    }

    private final int MOVIE_INFO = 1;
    private final int MOVIE_TRAILER = 2;
    private final int MOVIE_REVIEW =3;

    public ComplexMovieAdaptor(ArrayList<Object> items, OnItemClicked listener){
        mItems = items;
        mItemClicked = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder  viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case MOVIE_INFO:
                View v1 = inflater.inflate(R.layout.movie_detail_holder, parent, false);
                viewHolder = new MovieInfoDetailHolder(v1);
                break;
            case MOVIE_TRAILER:
                View v2 = inflater.inflate(R.layout.trailer_view_holder, parent, false);
                viewHolder = new MovieTrailerHolder(v2);
                break;
            case MOVIE_REVIEW:
                View v3 = inflater.inflate(R.layout.review_view_holder, parent, false);
                viewHolder = new MovieReviewHolder(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.add_reviews_holder, parent, false);
                viewHolder = new SimpleViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case MOVIE_INFO:
                MovieInfoDetailHolder v1 = (MovieInfoDetailHolder) holder;
                configureMovieInforHolder(v1, position);
                break;
            case MOVIE_TRAILER:
                MovieTrailerHolder v2 = (MovieTrailerHolder) holder;
                configureMovieTrailerHolder(v2, position);
                break;
            case MOVIE_REVIEW:
                MovieReviewHolder v3 = (MovieReviewHolder) holder;
                configureMovieReviewHolder(v3, position);
                break;
            default:
                break;
        }

    }

    private void configureMovieReviewHolder(MovieReviewHolder v3, int position) {
        MovieReviewObject reviewObject = (MovieReviewObject) mItems.get(position);
        v3.getAuthor().setText(reviewObject.getAuthorName());
        v3.getReview().setText(reviewObject.getReview());
    }

    private void configureMovieTrailerHolder(MovieTrailerHolder v2, int position) {
        TrailerInfoObject movieTrailerHolder = (TrailerInfoObject) mItems.get(position);
        v2.getTrailerTextView().setText(movieTrailerHolder.getTrailerName());
        v2.getTrailerPlayButton().setContentDescription(movieTrailerHolder.getTrailerKey());
    }

    private void configureMovieInforHolder(MovieInfoDetailHolder v1, int position) {

        MovieInfoDetailObject movie = (MovieInfoDetailObject) mItems.get(position);
        //Setup Movie poster
        Context context = v1.mPoster.getContext();
        ImageView imageView = new ImageView(context);
        Picasso picasso = Picasso.with(context);
        picasso.load(UriBuilderUtil.imageDownload(movie.getMoviePoster()))
           .into(imageView);
        v1.getPoster().setImageDrawable(imageView.getDrawable());
        //set title
        v1.getMovieTitle().setText(movie.getMovieTitle());
        //set release date
        v1.getMovieReleaseDate().setText(movie.getMovieReleaseDate());
        //set movie length;
        String text = context.getString(R.string.minutes_text, movie.getMovieLength());
        v1.getMovieLength().setText(text);
        //set movie rating
        v1.getMovieRating().setText(movie.getMovieRating());
        //set description
        v1.getMovieDescription().setText(movie.getMovieDescription());


        //Setup the favorite image
        if (movie.getFavorite()){
            v1.getFavorite().setVisibility(View.VISIBLE);
            v1.getNotFavorite().setVisibility(View.INVISIBLE);
        }else if (!movie.getFavorite()){
            v1.getFavorite().setVisibility(View.INVISIBLE);
            v1.getNotFavorite().setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position)instanceof MovieInfoDetailObject){
            return MOVIE_INFO;
        }else if (mItems.get(position)instanceof TrailerInfoObject){
            return MOVIE_TRAILER;
        }else if (mItems.get(position)instanceof MovieReviewObject){
            return MOVIE_REVIEW;
        }
        return -1;
    }
}
