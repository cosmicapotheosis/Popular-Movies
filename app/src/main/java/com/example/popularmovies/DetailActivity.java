package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieList;
import com.example.popularmovies.model.VideoList;
import com.example.popularmovies.model.Video;
import com.example.popularmovies.network.MovieService;
import com.example.popularmovies.network.RetrofitClientInstance;
import com.example.popularmovies.network.TrailerService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    Movie mMovie;
    private ArrayList<Video> mVideosList;
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

    private TrailerService service;

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

        service = RetrofitClientInstance.getRetrofitInstance().create(TrailerService.class);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Movie")) {
            mMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");
            populateUI();
            getVideosList();
        }
    }

    private void getVideosList() {
        // API key stored in ~/.gradle/gradle.properties
        // Movie id must be converted to string.
        Call<VideoList> call = service.getTrailerVideos(String.valueOf(mMovie.getId()), BuildConfig.ApiKey);
        call.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                if (response.isSuccessful()) {
                    // save the response as a list of movies
                    mVideosList = response.body().getVideoArrayList();
                    // do something
//                    for (int i = 0; i < mVideosList.size(); i++) {
//                        Video video = mVideosList.get(i);
//                        Log.d("DetailActivity", video.getName());
//                        Toast.makeText(DetailActivity.this, video.getName(), Toast.LENGTH_SHORT).show();
//                    }
                    // Set click listener for trailer link
                    assert mTrailerLink != null;
                    mTrailerLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            watchYoutubeVideo(DetailActivity.this, mVideosList.get(0).getKey());
                        }
                    });
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(DetailActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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