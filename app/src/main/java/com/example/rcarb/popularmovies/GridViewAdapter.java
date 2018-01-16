package com.example.rcarb.popularmovies;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rcarb.popularmovies.Utils.MovieInfoHolder;
import com.example.rcarb.popularmovies.Utils.UriBuilderUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ImageViewHolder> {

    private final int mNumberOfItems;
    private final ArrayList<MovieInfoHolder> mMovieArrayList;
    private final OnItemClicked mOnClick;

    public interface OnItemClicked {
        void onItemClick(int mNumberOfItemsIndex, int movieId);
    }


    public GridViewAdapter(int numberOfItems, ArrayList<MovieInfoHolder> movies,
                           OnItemClicked listener) {
        this.mNumberOfItems = numberOfItems;
        this.mMovieArrayList = movies;
        this.mOnClick = listener;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int viewToBeInflated = R.layout.image_view_holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldBeAttachedImeidately = false;

        View view = inflater.inflate(viewToBeInflated, viewGroup, shouldBeAttachedImeidately);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        params.height = viewGroup.getMeasuredHeight() / 2;
        view.setLayoutParams(params);


        return new ImageViewHolder(view);
    }


    /* This method, with the help of Picasso will download the image using the specified URI, and set it
    * and will set it to the ImageViewHolder's ImageView*/
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Context context = holder.mImageView.getContext();


        int movieId = mMovieArrayList.get(position).getMovieId();

        holder.mImageView.setContentDescription(Integer.toString(movieId));


        //String movieInfo = movieHolder.moviePoster;
        Picasso picasso = Picasso.with(context);
        picasso.load(UriBuilderUtil.imageDownload(mMovieArrayList.get(position).getMoviePoster()))
                .into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        //Just return the mNumberOfItems variable
        return mNumberOfItems;

    }

    //ViewHolder Class that will hold an imageView
    class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            //cast the ImageView.
            mImageView = itemView.findViewById(R.id.rv_image_view);
            //set the onClickListener on the itemView in the viewholder's constructor
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String movieId = (String) mImageView.getContentDescription();
            int movieIdInt = Integer.valueOf(movieId);
            mOnClick.onItemClick(clickedPosition, movieIdInt);
        }
    }
}