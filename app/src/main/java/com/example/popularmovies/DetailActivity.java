package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    Movie mMovie;
    // Use Butterknife to set views
    @BindView(R.id.count_tv) TextView mVoteCount;
    //TextView mId;
    //TextView mVideo;
    @BindView(R.id.rating_tv) TextView mVoteAverage;
    @BindView(R.id.pop_tv) TextView mPopularity;
    @BindView(R.id.poster_iv) ImageView mPoster;
    //TextView mOriginalLanguage;
    //TextView mOriginalTitle;
    //TextView mGenreIds;
    //ImageView mBackdrop;
    //TextView mAdult;
    @BindView(R.id.overview_tv) TextView mOverview;
    @BindView(R.id.year_tv) TextView mReleaseDate;
    @BindView(R.id.trailer_link_tv) TextView mTrailerLink;

    // TODO (1) Use Retrofit to return list of youtube links for movie.
    // Video and VideoList models
    // TrailerService
    // endpoint https://api.themoviedb.org/3/movie/324857/videos?api_key=8241740b7b3cbffc6476467a4bd4ed6f
    // TODO (2) Use new recyclerview adapter to display results in a list. each list item has a play icon and title "Trailer 1" etc.
    // very similar to how retrofit is used to populate the mainview with movie poster images

    /**
     * Check that a Movie object was passed as extra, then populate the UI based on that Movie data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Movie")) {
            mMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");
            populateUI();
            // Set click listener for trailer link
            assert mTrailerLink != null;
            mTrailerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchYoutubeVideo(DetailActivity.this, "HsFjDou_4qk");
                }
            });
        }
    }

    /**
     * Method to launch a Youtube video
     * @param context
     * @param id
     */
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    /**
     * Set the text and image views based on passed extra data.
     */
    private void populateUI() {
        setTitle(mMovie.getTitle());
        // set the poster
        String posterUrl = "http://image.tmdb.org/t/p/w500/" + mMovie.getPoster_path();
        // Use picasso to set the image view in the view holder
        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                //.fit()
                .into(mPoster);

        // Set text info
        mVoteAverage.setText(Float.toString(mMovie.getVote_average()) + "/10");
        mReleaseDate.setText(mMovie.getRelease_date().substring(0,4));
        mOverview.setText(mMovie.getOverview());
        mPopularity.setText(Float.toString(mMovie.getPopularity()));
        mVoteCount.setText("Out of " + mMovie.getVote_count() + " votes");
        mTrailerLink.setText("Watch Trailer");
    }
}