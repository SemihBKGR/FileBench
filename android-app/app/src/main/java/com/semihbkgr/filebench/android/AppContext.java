package com.semihbkgr.filebench.android;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semihbkgr.filebench.android.net.BenchClient;
import com.semihbkgr.filebench.android.net.BenchClientImpl;
import okhttp3.OkHttpClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppContext {

    public static AppContext instance;

    public final Gson gson;
    public final OkHttpClient httpClient;
    public final BenchClient benchClient;
    public final DateFormat dateFormat;

    private AppContext() {
        this.gson = new GsonBuilder().create();
        this.httpClient = new OkHttpClient.Builder().build();
        this.benchClient = new BenchClientImpl(Constants.SERVER_URL, httpClient, gson);
        this.dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.getDefault());
    }

    public static void initialize() {
        if (instance == null)
            instance = new AppContext();
    }

    public static class Constants {
        public static final String SERVER_URL = "http://192.168.1.2:9000";
        public static final long MAIN_ACTIVITY_LOGO_TIME_MS = 1_000L;
        public static final int BENCH_ID_LENGTH = 7;
        public static final String INTENT_EXTRA_BENCH = "bench";
    }

}
