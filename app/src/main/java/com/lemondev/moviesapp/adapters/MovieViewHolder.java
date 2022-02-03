package com.lemondev.moviesapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemondev.moviesapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title, vote_count, release_date;
    ImageView imageView;
    RatingBar ratingBar;

    //Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        title = itemView.findViewById(R.id.movie_title);
        release_date = itemView.findViewById(R.id.movie_release_date);
        vote_count = itemView.findViewById(R.id.movie_vote_count);
        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.movie_rate);

        this.onMovieListener = onMovieListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
