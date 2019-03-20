package com.example.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favorites;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the favorites from the DataBase");
        favorites = database.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }
}