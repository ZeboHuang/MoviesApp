package com.lemondev.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.lemondev.moviesapp.utils.MovieApi;

public class MovieModel implements Parcelable {
    // Model class for movies

    @SerializedName("id")
    private int movie_id;

    private String imdb_id;

    private String poster_path;

    @SerializedName("overview")
    private String movie_overview;

    private String release_date;

    private int runtime;

    private String title;

    private float vote_average;

    private int vote_count;

    public MovieModel(int movie_id, String imdb_id, String poster_path, String movie_overview, String release_date, int runtime, String title, float vote_average, int vote_count) {
        this.movie_id = movie_id;
        this.imdb_id = imdb_id;
        this.poster_path = poster_path;
        this.movie_overview = movie_overview;
        this.release_date = release_date;
        this.runtime = runtime;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    private MovieModel(Parcel in) {
        movie_id = in.readInt();
        imdb_id = in.readString();
        poster_path = in.readString();
        movie_overview = in.readString();
        release_date = in.readString();
        runtime = in.readInt();
        title = in.readString();
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }


    public static final Parcelable.Creator<MovieModel> CREATOR =
            new Parcelable.Creator<MovieModel>() {

                @Override
                public MovieModel createFromParcel(Parcel source) {
                    return new MovieModel(source);
                }

                @Override
                public MovieModel[] newArray(int size) {
                    return new MovieModel[size];
                }
            };

    //Getters

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public int getVote_count() {
        return vote_count;
    }
    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(imdb_id);
        dest.writeString(poster_path);
        dest.writeString(movie_overview);
        dest.writeString(release_date);
        dest.writeInt(runtime);
        dest.writeString(title);
        dest.writeFloat(vote_average);
        dest.writeInt(vote_count);
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "movie_id=" + movie_id +
                ", imdb_id='" + imdb_id + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", movie_overview='" + movie_overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", title='" + title + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                '}';
    }
}
