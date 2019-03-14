package com.example.popularmovies.network;

import com.example.popularmovies.model.ReviewList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {

    // /3/movie/324857/reviews?api_key=dfgfdgfdgdfgdfg
    @GET("/3/movie/{id}/reviews")
    Call<ReviewList> getReviews(@Path("id") String id, @Query("api_key") String api_key);

}
