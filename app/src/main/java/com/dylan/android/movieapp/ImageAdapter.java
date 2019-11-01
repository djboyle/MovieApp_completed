package com.dylan.android.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Dyl on 21/05/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>{

    private String[][] mMovieData;
    private String[] mPosterData;

    /*
         * An on-click handler that we've defined to make it easy for an Activity to interface with
         * our RecyclerView
         */
    private final ImageAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ImageAdapterOnClickHandler {
        void onClick(String[] clickedMovieData);
    }
    /**
     * Creates a ImageAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ImageAdapter(ImageAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a image items.
     */
    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageView;


        public ImageAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_thumbnail);

            view.setOnClickListener(this);
        }
        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mMovieData[adapterPosition+1]);
        }
    }
    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ImageAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ImageAdapterViewHolder(view);
    }
    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie images
     *  for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param imageAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder imageAdapterViewHolder, int position) {
        Context context = imageAdapterViewHolder.mImageView.getContext();
        String BasePosterAddress= "http://image.tmdb.org/t/p/w185/";
        String ImageInfo = mPosterData[position];

        Picasso.with(context).load(BasePosterAddress + ImageInfo).into(imageAdapterViewHolder.mImageView);
    }
    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mPosterData) return 0;
        return mPosterData.length;
    }

    /**
     * This method is used to set the movie data on a ImageAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ImageAdapter to display it.
     *
     * @param mData The new movie data to be used by the imageAdapter
     */
    public void setImageData(String[][] mData) {
        mMovieData = mData;
        mPosterData = mMovieData[0];
        notifyDataSetChanged();
    }



    }
