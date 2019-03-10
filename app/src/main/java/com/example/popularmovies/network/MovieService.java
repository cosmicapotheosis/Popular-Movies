package com.example.popularmovies.network;

import com.example.popularmovies.model.MovieList;
import com.example.popularmovies.model.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("/3/movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String api_key);

    @GET("/3/movie/top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("/3/movie/{id}/videos")
    Call<VideoList> getTrailerVideos(@Path("id") String id, @Query("api_key") String api_key);

}
