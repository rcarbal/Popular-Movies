package com.example.rcarb.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.rcarb.popularmovies.R;

/**
 * Created by rcarb on 3/29/2018.
 */

public class MovieReviewHolder extends RecyclerView.ViewHolder {

    private TextView mAuthor;
    private TextView mReview;

    public MovieReviewHolder(View itemView) {
        super(itemView);
        mAuthor = itemView.findViewById(R.id.author_tv);
        mReview = itemView.findViewById(R.id.reviews_tv);
    }

    public void setAuthor(TextView author){
        mAuthor = author;
    }
    public TextView getAuthor(){
        return mAuthor;
    }
    public void setReview(TextView review){
        mReview = review;
    }
    public TextView getReview(){
        return mReview;
    }
}
