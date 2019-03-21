package com.example.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieList;
import com.example.popularmovies.network.MovieService;
import com.example.popularmovies.network.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.ListItemClickListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_posters) RecyclerView mRecyclerView;

    private MoviePosterAdapter mMoviePosterAdapter;

    private ArrayList<Movie> mMoviesList;

    private MovieService service;

    private AppDatabase mDb;

    // flag to keep track of whether or not we are viewing favorites
    private Boolean showFavorites = false;

    private static final String LIFECYCLE_MOVIES_TEXT_KEY = "movies";


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

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_MOVIES_TEXT_KEY)) {
                ArrayList<Movie> previouslyViewedMovies = savedInstanceState
                        .getParcelableArrayList(LIFECYCLE_MOVIES_TEXT_KEY);
                mMoviesList = previouslyViewedMovies;
                // only display 20 posters
                String[] posters = getPosters(mMoviesList);
                mMoviePosterAdapter.setMoviePosterUrls(posters);
            }
        } else {
            // To begin with, sort movies by popular
            getPopularMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIFECYCLE_MOVIES_TEXT_KEY, mMoviesList);
    }

    /**
     * Retrieve list of favorites from db and display them if showFavorites flag is set to true
     */
    private void getFavoriteMovies() {
        // View model implementation
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (showFavorites) {
                    // Only if view is set to display favorites should we update the adapter to hold the favorites
                    mMoviesList = new ArrayList<Movie>(movies);
                    Log.d(TAG, "Receiving database update from LiveData in ViewModel");
                    // only display 20 posters
                    String[] posters = getPosters(mMoviesList);
                    mMoviePosterAdapter.setMoviePosterUrls(posters);
                }
            }
        });
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
            showFavorites = false;
            getPopularMovies();
            return true;
        } else if (itemThatWasClickedId == R.id.sort_by_rating) {
            // make network request and populate grid views..
            Context context = MainActivity.this;
            String textToShow = "Sorting movies by rating...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            showFavorites = false;
            getTopRatedMovies();
            return true;
        } else if (itemThatWasClickedId == R.id.list_favorites) {
            Context context = MainActivity.this;
            String textToShow = "Showing favorite movies...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            // Allow adapter to be set with favorites
            showFavorites = true;
            getFavoriteMovies();
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
                    String[] posters = getPosters(mMoviesList);
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
                    String[] posters = getPosters(mMoviesList);
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

    /**
     * Helper method returns array of posters from movies list to populate adapter
     * @param movies
     * @return
     */
    private String[] getPosters(ArrayList<Movie> movies) {
        String[] posters;

        if (movies.size() > 20) {
            posters = new String[20];
        } else {
            posters = new String[movies.size()];
        }

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            String posterPath = movie.getPoster_path();
            posters[i] = "http://image.tmdb.org/t/p/w185/" + posterPath;
        }
        return posters;
    }
}
