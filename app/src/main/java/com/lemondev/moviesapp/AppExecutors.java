package com.lemondev.moviesapp;

import com.lemondev.moviesapp.request.MovieApiClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    //running service in background
    //

    //pattern

    private static AppExecutors instance;


    public static AppExecutors getInstance() {
        if (instance == null) {
            synchronized (AppExecutors.class) {
                if (instance == null) {
                    instance = new AppExecutors();
                }
            }
        }
        return instance;
    }


    //calls the retrofit in background thread
    //java 定时调度机制


    //单个线程池就不会     Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService mNetworkIO = Executors.newSingleThreadScheduledExecutor();


    //会中断异常
//    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);


    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }





}
