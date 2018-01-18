package com.example.rcarb.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rcarb.popularmovies.Utils.MovieReviewObject;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class MovieReviewsAdaptor extends RecyclerView.Adapter<MovieReviewsAdaptor.ReviewViewHolder>{

    private final ArrayList<MovieReviewObject> reviews;
    private final int sizeOfData;

    public MovieReviewsAdaptor(int size, ArrayList<MovieReviewObject> movieReviews){
        reviews = movieReviews;
        sizeOfData = size;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int viewToBeInflated = R.layout.review_view_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldBeImidiatelyAttached = false;

        View view = layoutInflater.inflate(viewToBeInflated, parent, shouldBeImidiatelyAttached);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        String author = reviews.get(position).getAuthorName();
        String review = reviews.get(position).getReview();

        holder.mAuthorTextView.setText(author);
        holder.mReviewsTextView.setText(review);



    }

    @Override
    public int getItemCount() {
        return sizeOfData;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        final TextView mAuthorTextView;
        final TextView mReviewsTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mAuthorTextView = itemView.findViewById(R.id.author_tv);
            mReviewsTextView = itemView.findViewById(R.id.reviews_tv);
        }
    }
}
