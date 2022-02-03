package com.lemondev.moviesapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemondev.moviesapp.R;

public class PopMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    RatingBar ratingBar;

    //Click Listener
    OnMovieListener onMovieListener;

    public PopMovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        imageView = itemView.findViewById(R.id.pop_movie_img);
        ratingBar = itemView.findViewById(R.id.ratingbar_pop);

        this.onMovieListener = onMovieListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
