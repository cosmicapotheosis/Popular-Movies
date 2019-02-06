package com.example.popularmoviesstage1.network;

import com.example.popularmoviesstage1.model.Movie;
import com.example.popularmoviesstage1.model.MovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("/3/movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String api_key);

    @GET("/3/movie/top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String api_key);

}
