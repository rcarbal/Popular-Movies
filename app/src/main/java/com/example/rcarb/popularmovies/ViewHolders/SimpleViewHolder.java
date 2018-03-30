package com.example.rcarb.popularmovies.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.rcarb.popularmovies.R;

/**
 * Created by rcarb on 3/29/2018.
 */

public class SimpleViewHolder extends RecyclerView.ViewHolder {

    private View mImageView;
    public SimpleViewHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.line);
    }
}
