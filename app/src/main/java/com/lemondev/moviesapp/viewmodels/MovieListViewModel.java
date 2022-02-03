package com.lemondev.moviesapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    //this class is used for VIEWMODEL

    //根据MVVM 的依赖图，来看，    ViewModel关联 Repository 仓库层，，so 在这获取仓库层的实例

    private MovieRepository movieRepository = MovieRepository.getInstance();


    public MovieListViewModel() {

    }

    public LiveData<List<MovieModel>> getMovies() {
        Log.v("Tag", "ViewModel getMovies is null? " + (movieRepository.getMovies() == null));
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return movieRepository.getMoviesPop();
    }

    //3- Call method in ViewModel
    public void searchMovieApi(String query, int pageNumber) {
        Log.v("Tag", "In ViewModel");

        MovieRepository.getInstance().searchMovieApi(query, pageNumber);
    }

    //search for next page
    public void searchNextPage() {
        MovieRepository.getInstance().searchNextPage();
    }

    public void searchMoviesPop( int pageNumber) {
        MovieRepository.getInstance().searchMoviesPop(pageNumber);
    }

    //search for next page
    public void searchNextPopPage() {
        MovieRepository.getInstance().searchNextPopPage();
    }
}
