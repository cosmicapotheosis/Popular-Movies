package com.example.popularmovies.network;

import com.example.popularmovies.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("/3/movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String api_key);

    @GET("/3/movie/top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String api_key);

}
