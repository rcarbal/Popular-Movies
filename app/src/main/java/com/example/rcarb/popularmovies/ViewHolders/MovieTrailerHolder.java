package com.example.rcarb.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.popularmovies.R;

/**
 * Created by rcarb on 3/29/2018.
 */

public class MovieTrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mTrailerTextview;
    private ImageView mTrailerButton;

    public MovieTrailerHolder(View itemView) {
        super(itemView);
        mTrailerButton = itemView.findViewById(R.id.trailer_button);
        mTrailerTextview = itemView.findViewById(R.id.trailer_textview_holder);
        itemView.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
    }
}
