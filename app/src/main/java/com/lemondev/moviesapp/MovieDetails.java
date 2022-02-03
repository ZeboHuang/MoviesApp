package com.lemondev.moviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.utils.Credentials;
import com.lemondev.moviesapp.utils.MovieApi;

public class MovieDetails extends AppCompatActivity {

    //Wigets
    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails, voteAvgDetails;
    private RatingBar ratingBarDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        imageViewDetails = findViewById(R.id.imageView_details);
        titleDetails = findViewById(R.id.textview_title_details);
        descDetails = findViewById(R.id.textView_describe_details);
        ratingBarDetails = findViewById(R.id.ratingbar_details);
        voteAvgDetails = findViewById(R.id.textView_vote_avg_details);


        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            /**
             * getExtra()
             * getParcelableExtra()
             *  区别：
             *
             */
            MovieModel movieModel = getIntent().getParcelableExtra("movie");

            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating(movieModel.getVote_average() / 2);
            voteAvgDetails.setText(String.valueOf(movieModel.getVote_average()));
            Glide.with(this).load(Credentials.BASE_PIC_URL + movieModel.getPoster_path()).into(imageViewDetails);


            Log.d("TAG", "GetDataFromIntent: movie id: " + movieModel.toString());

        }


    }
}