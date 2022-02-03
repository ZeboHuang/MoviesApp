package com.lemondev.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.lemondev.moviesapp.adapters.MovieRecyclerViewAdapter;
import com.lemondev.moviesapp.adapters.OnMovieListener;
import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.repositories.MovieRepository;
import com.lemondev.moviesapp.request.Servicey;
import com.lemondev.moviesapp.response.MovieSearchResponse;
import com.lemondev.moviesapp.utils.Credentials;
import com.lemondev.moviesapp.utils.MovieApi;
import com.lemondev.moviesapp.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListActivity extends AppCompatActivity implements OnMovieListener {
    //添加网络安全配置 Network Security Configuration


    private final String TAG = "MovieListActivity";

    //RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;


    //ViewModel
    private MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView
        SetupSearchView();


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        recyclerView = findViewById(R.id.movie_recyclerview);

        //Initialize RecyclerView & Adding Data
        ConfigureRecyclerView();
        //Calling the Observer
        ObserveAnyChange();
        ObservePopularMovies();
        RefreshPopularMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_pop:
                RefreshPopularMovies();
                return true;
            default:
                return false;
        }
    }

    private void RefreshPopularMovies() {
        Credentials.POPULAR = true;
        movieListViewModel.searchMoviesPop(1);
    }

    private void ObservePopularMovies() {
        movieListViewModel.getMoviesPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    movieRecyclerViewAdapter.setmMovies(movieModels);
                }
            }
        });
    }


//    //4- call method in main activity
//    private void searchMovieApi(String query, int pageNumber) {
//        Log.v("Tag", "in ModelViewListActivity");
//        movieListViewModel.searchMovieApi(query, pageNumber);
//    }


    //Observing any data change
    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels
                    ) {
                        Log.v("Tag", "" + movieModel.getTitle() + "   " + movieModel.getRelease_date()
                                + "    get poster path: " + movieModel.getPoster_path() + "   getImdbID: " + movieModel.getImdb_id());
                    }
                    movieRecyclerViewAdapter.setmMovies(movieModels);
                }
            }
        });


    }

    //Initialize RecyclerView & Adding Data
    private void ConfigureRecyclerView() {
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //RecyclerView Pagination
        //loading next page of api response

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    //如果不能再向上滑动，加载下一页
                    if (Credentials.POPULAR) {
                        movieListViewModel.searchNextPopPage();
                    } else {
                        movieListViewModel.searchNextPage();
                    }
                }
            }
        });

    }


//    private void GetRetrofitResponse() {
//        MovieApi movieApi = Servicey.getMovieApi();
//
//        Call<MovieSearchResponse> responseCall = movieApi
//                .searchMovie(Credentials.API_KEY,
//                        "Jack Reacher",
//                        1
//                );
//
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                Log.d(TAG, "onResponse: responseCode: " + response.code());
//                if (response.code() == 200) {
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//
//
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//                    Log.d(TAG, "onResponse: movies is null? " + (movies == null));
//                    Log.d(TAG, "onResponse: movies size: " + movies.size());
//
//                    for (MovieModel m :
//                            movies) {
//                        Log.d(TAG, "onResponse: " + m.getTitle());
//                    }
//
//                } else {
//                    Log.d(TAG, "Error: " + response.errorBody().toString());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//
//    }
//
//    private void GetRetrofitResponseAccordingToID() {
//        MovieApi movieApi = Servicey.getMovieApi();
//        Call<MovieModel> call = movieApi.getMovie(
//                550,
//                Credentials.API_KEY
//        );
//        call.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if (response.code() == 200) {
//                    Log.d(TAG, "onResponse: ");
//                    MovieModel movieModel = response.body();
//                    Log.d(TAG, "onResponse: " + movieModel.getTitle());
//                } else {
//                    Log.d(TAG, "Error: " + response.errorBody().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//    }

    @Override
    public void onMovieClick(int position) {
        Log.d("TAG", "movie click");

        //We need get the movie id to get its details

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }


    //Get data from searchview & query the api to get the results
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Credentials.POPULAR = false;

                movieListViewModel.searchMovieApi(
                        query,
                        1);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }
}