package com.example.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoviePosterAdapter extends
        RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {

    // The URLS received from moviedb API
    private String[] mMoviePosterUrls;

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MoviePosterAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviePosterAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie poster
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param moviePosterAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder moviePosterAdapterViewHolder, int position) {
        String posterUrl = mMoviePosterUrls[position];
        // Use picasso to set the image view in the view holder
        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(moviePosterAdapterViewHolder.mMoviePosterImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movies list
     */
    @Override
    public int getItemCount() {
        if (null == mMoviePosterUrls) return 0;
        return mMoviePosterUrls.length;
    }

    /**
     * Sets the urls for the
     *
     * @param moviePosterUrls
     */
    public void setMoviePosterUrls(String[] moviePosterUrls) {
        mMoviePosterUrls = moviePosterUrls;
        notifyDataSetChanged();
    }

    public class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMoviePosterImageView;

        public MoviePosterAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        }
    }
}
