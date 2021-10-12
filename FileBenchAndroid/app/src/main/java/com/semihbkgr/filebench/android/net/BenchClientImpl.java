package com.semihbkgr.filebench.android.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.dto.BenchCreateDto;
import com.semihbkgr.filebench.android.net.dto.BenchUpdateDto;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

public class BenchClientImpl implements BenchClient {

    private final OkHttpClient httpClient;
    private final Gson gson;

    public BenchClientImpl(@NonNull OkHttpClient httpClient, @NonNull Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    @Override
    public void getBench(String benchId, ClientCallback<? super Bench> callback) {
        Request request = new Request.Builder().url(BenchConstants.BENCH_GET_URI+"/"+benchId).get().build();
        enqueueBenchRequest(request,callback);
    }

    @Override
    public void createBench(@NonNull BenchCreateDto benchCreateDto, @NonNull ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(benchCreateDto));
        Request request = new Request.Builder().url(BenchConstants.BENCH_CREATE_URI).post(requestBody).build();
        enqueueBenchRequest(request,callback);
    }

    @Override
    public void updateBench(String benchId, String token, BenchUpdateDto benchUpdateDto, ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(benchUpdateDto));
        Request request = new Request.Builder().url(BenchConstants.BENCH_CREATE_URI+"/"+benchId+"?token="+token).post(requestBody).build();
        enqueueBenchRequest(request,callback);
    }

    private void enqueueBenchRequest(@NonNull Request request, @NonNull ClientCallback<? super Bench> callback){
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException{
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null)
                        throw new IllegalStateException("ResponseBody is null");
                    String responseJsonStr=responseBody.string();
                    if(response.code()/100==2)
                        callback.success(gson.fromJson(responseJsonStr, Bench.class));
                    else
                        callback.error(gson.fromJson(responseJsonStr, ErrorModel.class));
                }
            }
        });
    }


}
