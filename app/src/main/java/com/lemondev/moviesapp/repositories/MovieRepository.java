package com.lemondev.moviesapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;


    private String mQuery;      //暂存请求串，以便请求下一页内容时，再次使用

    private int mPageNumber;


    public static MovieRepository getInstance() {
        if (instance == null) {
            synchronized (MovieRepository.class) {
                if (instance == null) {
                    instance = new MovieRepository();
                }
            }
        }
        return instance;
    }

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return movieApiClient.getMoviesPop();
    }

    //2-Calling method in repository
    public void searchMovieApi(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;

        MovieApiClient.getInstance().searchMoviesApi(query, pageNumber);
    }

    public void searchNextPage() {
        searchMovieApi(mQuery, mPageNumber + 1);
    }


    public void searchMoviesPop(int pageNumber) {
        mPageNumber = pageNumber;
        MovieApiClient.getInstance().searchMoviesPop(pageNumber);
    }

    public void searchNextPopPage() {
        searchMoviesPop(mPageNumber + 1);
    }

}
