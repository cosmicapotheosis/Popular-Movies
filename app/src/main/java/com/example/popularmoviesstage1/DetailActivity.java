package com.example.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
    }
}