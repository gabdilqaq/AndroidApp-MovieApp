package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;
import com.example.movieapp.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //Singleton Pattern
    //Live Data for Search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //making global request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Live Data for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPopular;

    //making popular request
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPopular() {
        return mMoviesPopular;
    }


    public void searchMoviesApi(String query,int pageNumber) {
        if(retrieveMoviesRunnable!=null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling Retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);



    }


    public void searchMoviesApiPopular(int pageNumber) {
        if(retrieveMoviesRunnablePopular!=null){
            retrieveMoviesRunnablePopular = null;
        }

        retrieveMoviesRunnablePopular = new RetrieveMoviesRunnablePopular(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePopular);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling Retrofit call
                myHandler2.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);



    }


    //Retrieving data from RESTAPI by runnable class
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response
            try{
                Response response = getMovies(query,pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response.code()==200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber==1){
                        //Sending data to live data
                        //PostValue:used for background thread
                        //setValue: not for background
                        mMovies.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }
                else{
                    String error = response.errorBody().string();
                    Log.v("Tag","Error "+error);
                    mMovies.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
            }


        }
        //Search Method query
        private Call<MovieSearchResponse> getMovies (String query,int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }


    private class RetrieveMoviesRunnablePopular implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePopular( int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response
            try{
                Response response2 = getPopular(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response2.code()==200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber==1){
                        //Sending data to live data
                        //PostValue:used for background thread
                        //setValue: not for background
                        mMoviesPopular.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMoviesPopular.getValue();
                        currentMovies.addAll(list);
                        mMoviesPopular.postValue(currentMovies);
                    }
                }
                else{
                    String error = response2.errorBody().string();
                    Log.v("Tag","Error "+error);
                    mMoviesPopular.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
            }


        }
        //Search Method query
        private Call<MovieSearchResponse> getPopular (int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }
}






