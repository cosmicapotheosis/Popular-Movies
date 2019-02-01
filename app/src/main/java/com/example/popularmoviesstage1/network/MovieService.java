package com.example.popularmoviesstage1.network;

import com.example.popularmoviesstage1.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {


    @GET("/popular")
    Call<List<Movie>> getPopularMovies(@Query("api_key") String api_key);

    @GET("/top_rated")
    Call<List<Movie>> getTopRatedMovies(@Query("api_key") String api_key);

}
