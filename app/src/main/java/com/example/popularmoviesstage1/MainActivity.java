package com.example.popularmoviesstage1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmoviesstage1.model.Movie;
import com.example.popularmoviesstage1.network.MovieService;
import com.example.popularmoviesstage1.network.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    // API URLs
    // TODO (1) Get list of movies http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    //    or http://api.themoviedb.org/3/movie/top_rated?api_key=[YOUR_API_KEY]
    // TODO (2) from results, get poster_path from each result
    // TODO (3) append poster path to tmdb url https://image.tmdb.org/t/p/w185/[poster_path]
    // TODO (4) place posters into grid view using picasso

    @BindView(R.id.recyclerview_posters) RecyclerView mRecyclerView;

    private MoviePosterAdapter mMoviePosterAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviePosterAdapter = new MoviePosterAdapter();

        mRecyclerView.setAdapter(mMoviePosterAdapter);

        /*Create handle for the RetrofitInstance interface*/
        MovieService service = RetrofitClientInstance.getRetrofitInstance().create(MovieService.class);
        // put below into its own method and call the method
        Call<List<Movie>> call = service.getPopularMovies(Integer.toString(R.string.api_key));
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
//                progressDoalog.dismiss();
//                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        // For now just load a bunch of the same movie poster into the adapter
        // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
        String[] tempPosters = new String[20];
        for (int i = 0; i < tempPosters.length; i++) {
            tempPosters[i] = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        }
        mMoviePosterAdapter.setMoviePosterUrls(tempPosters);

        // make network request and populate grid views..
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.sort_by_popular) {
            // make network request and populate grid views..
            Context context = MainActivity.this;
            String textToShow = "Sorting movies by popularity...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemThatWasClickedId == R.id.sort_by_rating) {
            // make network request and populate grid views..
            Context context = MainActivity.this;
            String textToShow = "Sorting movies by rating...";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
