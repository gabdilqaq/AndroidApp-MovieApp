package com.example.movieapp.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Widgets
    TextView title,category,duration;
    ImageView imageView;
    RatingBar ratingBar;

    //Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(View itemView, OnMovieListener onMovieListener){
        super(itemView);
        title = itemView.findViewById(R.id.movie_title);
//        category = itemView.findViewById(R.id.movie_category);
        duration = itemView.findViewById(R.id.movie_duration);

        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);


        this.onMovieListener = onMovieListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
