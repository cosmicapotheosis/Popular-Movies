package com.example.popularmoviesstage1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

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
