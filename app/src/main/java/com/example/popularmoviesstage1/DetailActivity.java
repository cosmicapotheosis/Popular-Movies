package com.example.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.model.Movie;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    TextView mVoteCount;
    TextView mId;
    TextView mVideo;
    TextView mVoteAverage;
    TextView mTitle;
    TextView mPopularity;
    ImageView mPoster;
    TextView mOriginalLanguage;
    TextView mOriginalTitle;
    TextView mGenreIds;
    ImageView mBackdrop;
    TextView mAdult;
    TextView mOverview;
    TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Movie")) {
            Movie movie = intentThatStartedThisActivity.getParcelableExtra("Movie");
            setTitle(movie.getTitle());
            populateUI();
        }
    }

    private void populateUI() {

    }
}
