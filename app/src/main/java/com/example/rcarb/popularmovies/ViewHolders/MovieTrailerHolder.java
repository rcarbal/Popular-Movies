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

public class MovieTrailerHolder extends RecyclerView.ViewHolder{


    private TextView mTrailerTextview;
    private ImageView mTrailerButton;

    public MovieTrailerHolder(View itemView, final ComplexMovieAdaptor.OnItemClicked onItemClicked) {
        super(itemView);
        mTrailerButton = itemView.findViewById(R.id.trailer_button);
        mTrailerTextview = itemView.findViewById(R.id.trailer_textview_holder);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailerKey =(String) mTrailerButton.getContentDescription();
                onItemClicked.onItemClick(trailerKey, false);
            }
        });
    }

    public void setTrailerTextView(TextView trailer){
        mTrailerTextview = trailer;
    }
    public TextView getTrailerTextView(){
        return mTrailerTextview;
    }
    public void setTrailerPlayButton(ImageView imageView){
        mTrailerButton = imageView;
    }
    public ImageView getTrailerPlayButton(){
        return  mTrailerButton;
    }

}
