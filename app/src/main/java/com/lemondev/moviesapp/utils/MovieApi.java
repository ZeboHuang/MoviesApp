package com.lemondev.moviesapp.utils;

import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    // Search for movies
    //https://api.themoviedb.org/3/search/movie?api_key=a4ce1f3a61e423f35ec9f07274e7949c&query=Jack+Reacher
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String api_key,
            @Query("query") String query,
            @Query("page") int page
    );


    //get pop
//https://api.themoviedb.org/3/movie/popular?api_key=a4ce1f3a61e423f35ec9f07274e7949c&page=2

    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String api_key,
            @Query("page") int page
    );


}
