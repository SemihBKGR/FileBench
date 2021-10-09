package com.semihbkgr.filebench.android;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semihbkgr.filebench.android.net.BenchClient;
import com.semihbkgr.filebench.android.net.BenchClientImpl;
import okhttp3.OkHttpClient;

public class AppContext {

    public static AppContext instance;

    public final Gson gson;
    public final OkHttpClient httpClient;
    public final BenchClient benchClient;


    private AppContext(){
        this.gson=new GsonBuilder().create();
        this.httpClient=new OkHttpClient.Builder().build();
        this.benchClient=new BenchClientImpl(httpClient,gson);
    }

    public static void initialize(){
        if(instance==null)
            instance=new AppContext();
    }

    public static class Constants{
        public static final int BENCH_ID_LENGTH=7;
    }

}
