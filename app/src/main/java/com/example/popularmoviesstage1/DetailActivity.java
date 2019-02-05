package com.example.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.popularmoviesstage1.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static Movie EXTRA_MOVIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Movie")) {
            Movie movie = intentThatStartedThisActivity.getParcelableExtra("Movie");
            setTitle(movie.getTitle());
        }
    }
}
