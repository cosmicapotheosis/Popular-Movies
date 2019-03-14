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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    implements MovieTrailerAdapter.ListItemClickListener {

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

    @BindView(R.id.recyclerview_trailers) RecyclerView mRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    // TODO (1) Use Retrofit to return list of reviews for movie.
    // Review and ReviewList models
    // ReviewsService
    // endpoint https://api.themoviedb.org/3/movie/324857/reviews?api_key=dfgfdgdfdfgdf

//    {
//        "id": 324857,
//        "page": 1,
//        "results": [
//            {
//                "author": "trineo03",
//                    "content": "First of all, I love the animation style in this film. The animation in this film is styled to look like an actual comic book. I think this approach for this kind of film was an excellent choice because we’ve all seen the usual kind of animation but nothing like this. To tie in with the animation I need to talk about the action scenes in this film. These tie in with the animation because of the way the directors styled the action shots is to look like something in a comic panel. Most people don’t know who Miles Morales is and after this film, you’ll want to learn more about him because of how they made him so relatable in this film. He acts like a typical teen in this situation compared to other versions of Spider-Man. Other versions kind of just acted like they always knew how to use their powers when Miles struggles with his. Miles isn’t the only relatable character film all of them are. The creators of this film did a “Marvel”ous job at making each character in this film somebody at least one person in the audience can relate to. All of the voice actors did a great job in their respected roles but it would’ve been nice to have a returning voice to at least one of the Spider-Man. It would’ve been cool to hear a returning voice even if it was for a few seconds. I have to talk about the humour in this film. It isn’t overpowered in this film and I felt like it had just the right amount of humour that will make everybody laugh. They poke fun of things that wouldn’t make sense in a real movie and other Spider-Man movies. Not a single moment in this film felt rushed or slowed down every scene felt the right pace for a movie like this. The cinematography in this movie was spectacular. This is probably because of it looking like a comic book and how the lighting needs to match up with how it would look in an actual book. If you are a comic book junkie you’ll love all of the easter eggs in this film. Some of them just comic book readers will get but others a majority of people will understand. The music in this film is fantastic. Every song in this film isn’t overused and matches perfectly with the age of the character. Somebody Miles age would be listening to the type of music he listens to. And the music without lyrics helps increase the emotion in the film. Also, there are two end credit scenes that are worth waiting for. In the end, this film is perfect for everybody. I give Spider-Man: Into the Spider-Verse a 10/10.",
//                    "id": "5c17088b92514132ba0be321",
//                    "url": "https://www.themoviedb.org/review/5c17088b92514132ba0be321"
//            },
//            {
//                "author": "Gimly",
//                    "content": "It's true I liked it less than perhaps the vast majority of _Spider-Verse's_ audience, but this was still great, the animation enamouring, and the depth of its story and reference totally engaging. Not to me the best Spider-Man movie as many have said, (that honour still goes to _Homecoming_) but a blast all the same.\r\n\r\n_Final rating:★★★½ - I really liked it. Would strongly recommend you give it your time._",
//                    "id": "5c6b7a529251412fc40c2bb0",
//                    "url": "https://www.themoviedb.org/review/5c6b7a529251412fc40c2bb0"
//            }
//        ],
//        "total_pages": 1,
//        "total_results": 2
//    }

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
                getReviewsList();
            }
            // use movie info to populate UI
            populateUI();

        }
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
                    ArrayList<Review> responseReviews = response.body().getResults();

                    for (Review r : responseReviews) {
                      Log.d("logged", "review author: " + r.getAuthor());
                    }

//                    // set up trailers recycler view, will probably movie this somewhere better
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
//                    mRecyclerView.setLayoutManager(layoutManager);
//                    mRecyclerView.setHasFixedSize(true);
//                    mMovieTrailerAdapter = new MovieTrailerAdapter(mVideosList.size(), DetailActivity.this);
//                    mRecyclerView.setAdapter(mMovieTrailerAdapter);

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

                    // set up trailers recycler view, will probably movie this somewhere better
                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);
                    mMovieTrailerAdapter = new MovieTrailerAdapter(mVideosList.size(), DetailActivity.this);
                    mRecyclerView.setAdapter(mMovieTrailerAdapter);

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