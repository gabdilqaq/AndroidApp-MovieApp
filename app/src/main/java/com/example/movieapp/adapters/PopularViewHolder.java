package com.example.movieapp.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class PopularViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    //Widgets
    TextView title2,category,duration2;
    ImageView imageView2;
    RatingBar ratingBar2;

    //Click Listener
    OnMovieListener onMovieListener;
    public PopularViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;

        imageView2 = itemView.findViewById(R.id.movie_img_popular);
        ratingBar2 = itemView.findViewById(R.id.rating_bar_popular);
        title2 = itemView.findViewById(R.id.movie_title_popular);
//        category = itemView.findViewById(R.id.movie_category);
        duration2 = itemView.findViewById(R.id.movie_duration_popular);

        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
