package com.example.movieapp.repositories;

import android.app.DownloadManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.MovieApiClient;
import com.example.movieapp.utils.MovieApi;

import java.util.List;

public class MovieRepository {
    //This class is acting as repositories
    //Live Data
    private MovieApiClient movieApiClient ;

    private static MovieRepository instance;

    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }
    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPopular(){
        return movieApiClient.getMoviesPopular();
    }

    //Calling the method in repository
    public void searchMovie(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query,pageNumber);
    }
    public void searchMoviePopular( int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApiPopular(pageNumber);
    }

    public void searchNextPage(){
        searchMovie(mQuery,mPageNumber+1);
    }
    public void searchNextPagePopular(){
        searchMoviePopular(mPageNumber+1);
    }
}
