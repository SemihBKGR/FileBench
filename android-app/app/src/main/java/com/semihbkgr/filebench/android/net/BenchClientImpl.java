package com.semihbkgr.filebench.android.net;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.semihbkgr.filebench.android.model.Bench;
import okhttp3.*;

import java.io.IOException;

public class BenchClientImpl implements BenchClient {

    private final String url;
    private final OkHttpClient httpClient;
    private final Gson gson;

    public BenchClientImpl(@NonNull String url, @NonNull OkHttpClient httpClient, @NonNull Gson gson) {
        this.url = url;
        this.httpClient = httpClient;
        this.gson = gson;
    }

    @Override
    public void getBench(String benchId, ClientCallback<? super Bench> callback) {
        Request request = new Request.Builder().url(url + "/bench/" + benchId).get().build();
        enqueueRequest(request, callback);
    }

    @Override
    public void createBench(@NonNull Bench bench, @NonNull ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(bench));
        Request request = new Request.Builder().url(url + "/bench").post(requestBody).build();
        enqueueRequest(request, callback);
    }

    @Override
    public void uploadFile() {

    }

    @Override
    public void updateBench(String benchId, String token, Bench bench, ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(bench));
        Request request = new Request.Builder().url(url + "/bench/" + benchId + "?token=" + token).post(requestBody).build();
        enqueueRequest(request, callback);
    }

    private void enqueueRequest(@NonNull Request request, @NonNull ClientCallback<? super Bench> callback) {
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null)
                        throw new IllegalStateException("ResponseBody is null");
                    String responseJsonStr = responseBody.string();
                    if (response.code() / 100 == 2)
                        callback.success(gson.fromJson(responseJsonStr, Bench.class));
                    else
                        callback.error(gson.fromJson(responseJsonStr, ErrorModel.class));
                }
            }
        });
    }


}
