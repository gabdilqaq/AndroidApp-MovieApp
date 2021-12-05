    package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;


import com.example.movieapp.adapters.MovieRecyclerView;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.Servicey;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;
import com.example.movieapp.utils.MovieApi;
import com.example.movieapp.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //Before we run our app, we need to add the Network Security config
    
    //RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    //ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView
        SetupSearchView();


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();

        ObserveAnyChange();

        ObservePopularChange();

        //Getting the popular movies
        movieListViewModel.searchMovieApiPopular(1);

//        //Testing the method
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            // Displaying only the results of page 1
//            @Override
//            public void onClick(View v) {
//                 searchMovieApi("Fast",2);
//            }
//        });
    }

    // Observing popular movies changes
    private void ObservePopularChange() {
        movieListViewModel.getMoviesPopular().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                if(movieModels!=null){
                    for(MovieModel movieModel:movieModels){
                        //Getting data in log
//                        Log.v("Tag","onChanged: "+movieModel.getOverview());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }


    //Observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                 if(movieModels!=null){
                    for(MovieModel movieModel:movieModels){
                        //Getting data in log
                        Log.v("Tag","onChanged: "+movieModel.getOverview());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                 }
            }
        });
    }



    //Initializing recyclerView & adding data to it
    private void ConfigureRecyclerView(){
        //Live Data can not be passed via the constructor
        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        // Recyclerview pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    // Here we need to display the next search results on the next page of api
                    if(isPopular){
                        movieListViewModel.searchNextPagePopular();
                    }
                    else{
                        movieListViewModel.searchNextPage();
                    }
                }
            }
        });
    }


    private void GetRetrofitResponse() {
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                "Iron Man",
                1
        );
        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if(response.code() == 200){

//                    Log.v("Tag","the response "+response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for(MovieModel movie : movies){
                        Log.v("Tag","The name: "+movie.getTitle());
                    }
                }
                else{
                    try{
                        Log.v("Tag","Error"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void GetRetrofitResponseAccordingToId(){
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(
                        343611,
                        Credentials.API_KEY
                );
        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if(response.code()==200){
                    MovieModel movie = response.body();
                    Log.v("Tag","The Response: "+movie.getTitle());
                }
                else{
                    try{
                        Log.v("Tag","Error "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }


    @Override
    public void onMovieClick(int position) {
        // Toast.makeText(this, "The position "+position ,Toast.LENGTH_SHORT).show();
        // We don't need position of the movie in recyclerview
        // We need the ID of the movie in order to get all it's details
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie",movieRecyclerAdapter.getSelectedMovie(position));
//        Log.i("voca", movieRecyclerAdapter.getSelectedMovie(position).toString());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }


    // Get data from search view & query the api to get the results
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string getted from searchview
                        query,1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==""){
                    isPopular = true;
//                    return isPopular;
                }
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }

}