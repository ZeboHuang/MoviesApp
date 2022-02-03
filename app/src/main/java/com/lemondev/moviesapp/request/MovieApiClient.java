package com.lemondev.moviesapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lemondev.moviesapp.AppExecutors;
import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.repositories.MovieRepository;
import com.lemondev.moviesapp.response.MovieSearchResponse;
import com.lemondev.moviesapp.utils.Credentials;

import java.io.IOException;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //又将仓库曾的LiveData 迁移到 remote client 中
    //LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //making global runnable request
    private RetrieveMoviesRunnable retrieveMovieRunnable;


    //livedata for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;
    //making pop runnable
    private RetrieveMoviesRunnablePop retrieveMovieRunnablePop;


    public static MovieApiClient getInstance() {
        if (instance == null) {
            synchronized (MovieApiClient.class) {
                if (instance == null) {
                    instance = new MovieApiClient();
                }
            }
        }
        return instance;
    }


    private MovieApiClient() {
        mMovies = new MutableLiveData<List<MovieModel>>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    //1- call method in request
    public void searchMoviesApi(String query, int pageNumber) {
        //避免存在多个，耗费资源
        if (retrieveMovieRunnable != null) {
            retrieveMovieRunnable = null;   //java自动回收机制
        }

        retrieveMovieRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        /**
         * 测试
         *         new Thread(retrieveMovieRunnable).start();
         *
         *      Future貌似有点问题，抛出 中断异常
         */


        //立即执行
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMovieRunnable);

        //延迟执行
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: wait cancel ");
                // a timeout , 超时处理，避免上面那个一直再处理，
                //Canceling the retrofit
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MICROSECONDS);
    }

    public void searchMoviesPop(int pageNumber) {
        //避免存在多个，耗费资源
        if (retrieveMovieRunnablePop != null) {
            retrieveMovieRunnablePop = null;   //java自动回收机制
        }

        retrieveMovieRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        //立即执行
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMovieRunnablePop);

        //延迟执行
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: wait cancel ");
                // a timeout , 超时处理，避免上面那个一直再处理，
                //Canceling the retrofit
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MICROSECONDS);
    }


    //retrive data from REstApi by runnable class
    // We have 2 types of Queries,
    private class RetrieveMoviesRunnable implements Runnable {

        //Runnable 类是做啥的？
        //继承Runnable 类，意图是，在我们需要更新数据时，会在后台借助retrofit获取远程数据，当超时时，关闭该线程，避免资源耗费。


        //Api需要的参数
        private String query;
        private int pageNumber;
        boolean cancelRequest;  //很good啊，经常有个取消获取的操作


        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response objects

            try {
                Response response = searchMoviesCall(query, pageNumber).execute();   //这个不是后台线程执行（enqueue）,这个是立即执行

                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {

                    Log.v("Tag", "movieApiClient: response success.");
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());

                    if (pageNumber == 1) {
                        //Sending data to live data
                        //LiveData更新数据的两种方式
                        // PostValue: used for background thread
                        // SerValue: not for background thread, 只能在主线程中使用

//                        mMovies.setValue(null);

                        /**
                         * Sets the value. If there are active observers, the value will be dispatched to them.
                         * This method must be called from the main thread. If you need set a value from a background thread, you can use postValue(Object)
                         */

                        /**
                         * Posts a task to a main thread to set the given value. So if you have a following code executed in the main thread:
                         *        liveData.postValue("a");
                         *        liveData.setValue("b");
                         *
                         * The value "b" would be set at first and later the main thread would override it with the value "a".
                         * If you called this method multiple times before a main thread executed a posted task, only the last value would be dispatched.
                         */
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error: " + error);
                    mMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "run: IOException");
                mMovies.postValue(null);
            }


        }


        //Search Method / query
        private Call<MovieSearchResponse> searchMoviesCall(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private void cancelRequest() {
            cancelRequest = true;
        }

    }

    private class RetrieveMoviesRunnablePop implements Runnable {

        //Api需要的参数
        private int pageNumber;
        boolean cancelRequest;  //很good啊，经常有个取消获取的操作

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response objects
            try {
                Response response2 = getPopMoviesCall(pageNumber).execute();   //这个不是后台线程执行（enqueue）,这个是立即执行

                if (cancelRequest) {
                    return;
                }
                if (response2.code() == 200) {

                    Log.v("Tag", "movieApiClient: response success.");
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response2.body()).getMovies());

                    if (pageNumber == 1) {

                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    String error = response2.errorBody().string();
                    Log.v("Tag", "Error: " + error);
                    mMoviesPop.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "run: IOException");
                mMoviesPop.postValue(null);
            }


        }


        //Search Method / query
        private Call<MovieSearchResponse> searchMoviesCall(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private Call<MovieSearchResponse> getPopMoviesCall(int pageNumber) {
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }

        private void cancelRequest() {
            cancelRequest = true;
        }

    }


}
