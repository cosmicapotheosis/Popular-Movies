package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieList;
import com.example.popularmovies.network.MovieService;
import com.example.popularmovies.network.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.ListItemClickListener {

    @BindView(R.id.recyclerview_posters) RecyclerView mRecyclerView;

    private MoviePosterAdapter mMoviePosterAdapter;

    private ArrayList<Movie> mMoviesList;

    private MovieService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Lay out posters in a dynamic 2x10 grid
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMoviePosterAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mMoviePosterAdapter);

        // Create handle for the RetrofitInstance interface
        service = RetrofitClientInstance.getRetrofitInstance().create(MovieService.class);
        // To begin with, sort movies by popular
        getPopularMovies();
    }

    /**
     * Use the list index to define the Movie object that is passed as an extra to the detail activity.
     * Movie object implements parcelable in order to be passed as extra.
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = MainActivity.this;
        Class destinationActivity = DetailActivity.class;
        Intent startDetailActivityIntent = new Intent(context, destinationActivity);
        Movie movieToPass = mMoviesList.get(clickedItemIndex);
        startDetailActivityIntent.putExtra("Movie", movieToPass);
        startActivity(startDetailActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    /**
     * Allows the user to choose the sort criteria for the displayed posters.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.sort_by_popular) {
            // make network request and populate grid views..
            Context context = MainActivity.this;
            String textToShow = "Sorting movies by popularity...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            getPopularMovies();
            return true;
        } else if (itemThatWasClickedId == R.id.sort_by_rating) {
            // make network request and populate grid views..
            Context context = MainActivity.this;
            String textToShow = "Sorting movies by rating...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            getTopRatedMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call moviedb popular movies API and save the response to mMoviesList.
     * Iterate through mMoviesList and save the poster paths to the posters array.
     * Use posters array to populate recycler view.
     */
    private void getPopularMovies() {
        // API key stored in ~/.gradle/gradle.properties
        Call<MovieList> call = service.getPopularMovies(BuildConfig.ApiKey);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    // save the response as a list of movies
                    mMoviesList = response.body().getMovieArrayList();
                    // only display 20 posters
                    String[] posters = new String[20];

                    for (int i = 0; i < mMoviesList.size(); i++) {
                        Movie movie = mMoviesList.get(i);
                        String posterPath = movie.getPoster_path();
                        posters[i] = "http://image.tmdb.org/t/p/w185/" + posterPath;
                    }

                    mMoviePosterAdapter.setMoviePosterUrls(posters);
                // handle network errors here
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Call moviedb top rated movies API and save the response to mMoviesList.
     * Iterate through mMoviesList and save the poster paths to the posters array.
     * Use posters array to populate recycler view.
     */
    private void getTopRatedMovies() {
        Call<MovieList> call = service.getTopRatedMovies(BuildConfig.ApiKey);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    mMoviesList = response.body().getMovieArrayList();

                    String[] posters = new String[20];

                    for (int i = 0; i < mMoviesList.size(); i++) {
                        Movie movie = mMoviesList.get(i);
                        String posterPath = movie.getPoster_path();
                        posters[i] = "http://image.tmdb.org/t/p/w185/" + posterPath;
                    }

                    mMoviePosterAdapter.setMoviePosterUrls(posters);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
