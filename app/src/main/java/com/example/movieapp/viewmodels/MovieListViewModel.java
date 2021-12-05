package com.example.movieapp.viewmodels;

import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    //this class is used for VIEWMODEL

    private MovieRepository movieRepository;

    //Constructor
    public MovieListViewModel(){
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getMoviesPopular(){
        return movieRepository.getPopular();
    }

    // Calling method in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovie(query,pageNumber);
    }

    public void searchMovieApiPopular(int pageNumber){
        movieRepository.searchMoviePopular(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
    public void searchNextPagePopular(){
        movieRepository.searchNextPagePopular();
    }
}
