package com.example.rcarb.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.popularmovies.Utils.TrailerInfoHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcarb on 1/22/2018.
 */

public class MovieTrailerAdaptor extends RecyclerView.Adapter<MovieTrailerAdaptor.TrailerViewHolder> {

    private final List<TrailerInfoHolder> mTrailers;
    private final TrailerOnClickListener mOnclicked;

    public interface TrailerOnClickListener {
        void onClickTrailer(String movieKey);
    }

    public MovieTrailerAdaptor(List<TrailerInfoHolder> trailers,
                               TrailerOnClickListener listener) {
        mTrailers = trailers;
        mOnclicked = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int viewToBeInflated = R.layout.trailer_view_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldBeImidiatelyAttached = false;

        View view = layoutInflater.inflate(viewToBeInflated, parent, shouldBeImidiatelyAttached);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        String trailerId = mTrailers.get(position).getTrailerKey();
        String trailerName = mTrailers.get(position).getTrailerName();

        holder.mTrailerTextview.setText(trailerName);
        holder.mTrailerButton.setContentDescription(trailerId);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTrailerTextview;
        final ImageView mTrailerButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerButton = itemView.findViewById(R.id.trailer_button);
            mTrailerTextview = itemView.findViewById(R.id.trailer_textview_holder);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String imageMovieKey = (String) mTrailerButton.getContentDescription();
            mOnclicked.onClickTrailer(imageMovieKey);
        }
    }
}