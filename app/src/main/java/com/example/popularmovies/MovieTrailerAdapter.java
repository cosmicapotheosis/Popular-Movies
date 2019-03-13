package com.example.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieTrailerAdapter extends
        RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerAdapterViewHolder> {

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    private int mNumberItems;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieTrailerAdapter(int numberOfItems, ListItemClickListener listener) {
        mOnClickListener = listener;
        mNumberItems = numberOfItems;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieTrailerAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieTrailerAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie poster
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieTrailerAdapterViewHolder holder, int position) {
        holder.mTrailerTextView.setText("Trailer " + String.valueOf(position + 1));
        // Use picasso to set the image view in the view holder
        Picasso.get()
                .load(R.drawable.playicon)
                .resize(120,120)
                .into(holder.mPlayImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movies list
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mTrailerTextView;
        public final ImageView mPlayImageView;


        public MovieTrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTextView = (TextView) view.findViewById(R.id.trailer_title_tv);
            mPlayImageView = (ImageView) view.findViewById(R.id.play_icon_iv);
            view.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
