package com.example.movieapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;


    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        if (viewType==DISPLAY_SEARCH){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                    parent,
                    false);
            return new MovieViewHolder(view,onMovieListener);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_layout,
                    parent,
                    false);
            return new PopularViewHolder(view,onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

//        ((MovieViewHolder)holder).duration.setText(mMovies.get(i).getOriginal_language());

        int itemViewType = getItemViewType(i);
        if(itemViewType==DISPLAY_SEARCH){
            //vote average is over 10,and our rating bar is over 5 stars:
            ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(i).getVote_average())/2);
            ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
            ((MovieViewHolder)holder).duration.setText(mMovies.get(i).getRelease_date());

            //ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovies.get(i).getPoster_path())
                    .into( (   (MovieViewHolder) holder ).imageView    );
        }else{
            //vote average is over 10,and our rating bar is over 5 stars:
            ((PopularViewHolder)holder).ratingBar2.setRating((mMovies.get(i).getVote_average())/2);
            ((PopularViewHolder)holder).title2.setText(mMovies.get(i).getTitle());
            ((PopularViewHolder)holder).duration2.setText(mMovies.get(i).getRelease_date());
            //ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovies.get(i).getPoster_path())
                    .into( (   (PopularViewHolder) holder ).imageView2    );
        }

    }

    @Override
    public int getItemCount() {
        if(mMovies!=null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    // Getting the ID of the movie clicked
    public MovieModel getSelectedMovie(int position){
        if(mMovies!=null){
            if (mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
            return null;
    }
    @Override
    public int getItemViewType(int position){
        if(Credentials.POPULAR){
            return DISPLAY_POP;
        }
        else{
            return DISPLAY_SEARCH;
        }
    }
}
