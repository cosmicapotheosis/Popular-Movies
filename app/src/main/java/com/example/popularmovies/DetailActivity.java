package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.ReviewList;
import com.example.popularmovies.model.VideoList;
import com.example.popularmovies.model.Video;
import com.example.popularmovies.network.RetrofitClientInstance;
import com.example.popularmovies.network.ReviewService;
import com.example.popularmovies.network.TrailerService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity
    implements MovieTrailerAdapter.ListItemClickListener,
        MovieReviewAdapter.ListItemClickListener {

    // Use Butterknife to set views
    @BindView(R.id.count_tv) TextView mVoteCount;
    @BindView(R.id.rating_tv) TextView mVoteAverage;
    @BindView(R.id.pop_tv) TextView mPopularity;
    @BindView(R.id.poster_iv) ImageView mPoster;
    @BindView(R.id.overview_tv) TextView mOverview;
    @BindView(R.id.year_tv) TextView mReleaseDate;
    //@BindView(R.id.trailer_link_tv) TextView mTrailerLink;

    private TrailerService trailerService;
    private ReviewService reviewService;

    private Movie mMovie;
    private ArrayList<Video> mVideosList = new ArrayList<Video>();
    private ArrayList<Review> mReviewsList = new ArrayList<Review>();

    @BindView(R.id.recyclerview_trailers) RecyclerView mRecyclerViewTrailers;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    @BindView(R.id.recyclerview_reviews) RecyclerView mRecyclerViewReviews;
    private MovieReviewAdapter mMovieReviewAdapter;

    // Member variable for the Database
    private AppDatabase mDb;

    /**
     * Check that a Movie object was passed as extra, then populate the UI based on that Movie data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        trailerService = RetrofitClientInstance.getRetrofitInstance().create(TrailerService.class);
        reviewService = RetrofitClientInstance.getRetrofitInstance().create(ReviewService.class);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Movie")) {
            // save movie info to member variable
            mMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");
            // Only if VideosList is empty do we make an API call
            // There may be videos with no Trailers to list, this doesn't account for that
            if (mVideosList.size() == 0) {
                getVideosList();
            }
            if (mReviewsList.size() == 0) {
                getReviewsList();
            }
            // set up trailers recycler view
            LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(DetailActivity.this);
            mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
            mRecyclerViewTrailers.setHasFixedSize(true);
            mRecyclerViewTrailers.setNestedScrollingEnabled(false);
            // initial filler adapter
            mMovieTrailerAdapter = new MovieTrailerAdapter(0, DetailActivity.this);
            mRecyclerViewTrailers.setAdapter(mMovieTrailerAdapter);
            // set up reviews recycler view
            LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(DetailActivity.this);
            mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);
            //mRecyclerViewReviews.setHasFixedSize(true);
            mRecyclerViewReviews.setNestedScrollingEnabled(false);
            // initial filler adapter
            mMovieReviewAdapter = new MovieReviewAdapter(mReviewsList);
            mRecyclerViewReviews.setAdapter(mMovieReviewAdapter);
            // use movie info to populate UI
            populateUI();

            mDb = AppDatabase.getInstance(getApplicationContext());
        }
    }

    /**
     * Called when the user touches the "MARK AS FAVORITE"  button
     */
    public void addFavorite(View view) {
        // Do something in response to button click
        Toast.makeText(DetailActivity.this, "Movie added to favorites!", Toast.LENGTH_SHORT).show();
        mDb.movieDao().insertMovie(mMovie);
    }

    /**
     * Makes API call to themoviedb using the ID of the movie to retrieve a list of Review objects
     * and save them to the mReviewsList variable
     */
    private void getReviewsList() {
        // API key stored in ~/.gradle/gradle.properties
        // Movie id must be converted to string.
        Call<ReviewList> call = reviewService.getReviews(String.valueOf(mMovie.getId()), BuildConfig.ApiKey);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if (response.isSuccessful()) {

                    // iterate through response
                    mReviewsList = response.body().getResults();

                    // Update reviews recyclerview
                    mMovieReviewAdapter = new MovieReviewAdapter(mReviewsList);
                    mRecyclerViewReviews.setAdapter(mMovieReviewAdapter);

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
            public void onFailure(Call<ReviewList> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Makes API call to themoviedb using the ID of the movie to retrieve a list of Video objects
     * and save them to the mVideosList variable if they are of the "Trailer" type
     */
    private void getVideosList() {
        // API key stored in ~/.gradle/gradle.properties
        // Movie id must be converted to string.
        Call<VideoList> call = trailerService.getTrailerVideos(String.valueOf(mMovie.getId()), BuildConfig.ApiKey);
        call.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                if (response.isSuccessful()) {

                    // iterate through response and save only the trailers to the VideosList
                    ArrayList<Video> responseVideos = response.body().getVideoArrayList();
                    for (Video vid : responseVideos) {
                        if (vid.getType().equals("Trailer")) {
                            mVideosList.add(vid);
                        }
                    }

                    mMovieTrailerAdapter = new MovieTrailerAdapter(mVideosList.size(), DetailActivity.this);
                    mRecyclerViewTrailers.setAdapter(mMovieTrailerAdapter);

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
        //mTrailerLink.setText("Watch Trailer");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String videoKey = mVideosList.get(clickedItemIndex).getKey();
        watchYoutubeVideo(DetailActivity.this, videoKey);
    }
}