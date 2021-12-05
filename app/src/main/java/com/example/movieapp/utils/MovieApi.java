package com.example.movieapp.utils;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;

import java.net.URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieApi {
    //Search for movie
    @GET("/3/search/movie")

    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );


    // Get popular movie
    // https://api.themoviedb.org/3/movie/popular?api_key=00d5e9eb94fedbba6b8afe75c98bfe77&page=1
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );






    //Making search with ID
    //https://api.themoviedb.org/3/movie/550?api_key=00d5e9eb94fedbba6b8afe75c98bfe77
    //Remember that movie_id = 550 is for Jack Reacher movie
    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

}
