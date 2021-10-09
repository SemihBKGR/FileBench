package com.semihbkgr.filebench.android.net;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.dto.BenchCreateDto;
import okhttp3.*;

import java.io.IOException;

public class BenchClientImpl implements BenchClient {

    private final OkHttpClient httpClient;
    private final Gson gson;

    public BenchClientImpl(@NonNull OkHttpClient httpClient, @NonNull Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    @Override
    public void getBench(String benchId, ClientCallback<? super Bench> callback) {
        Request request = new Request.Builder().url(BenchConstants.BENCH_CREATE_URI).get().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody responseBody=response.body()){
                    if(responseBody==null)
                        throw new IllegalStateException("ResponseBody is null");
                    String responseStr=responseBody.string();
                    Bench bench=gson.fromJson(responseStr,Bench.class);
                    callback.success(bench);
                }catch (Exception e){
                    callback.fail(e);
                }
            }
        });
    }

    @Override
    public void createBench(@NonNull BenchCreateDto benchCreateDto, @NonNull ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(benchCreateDto));
        Request request = new Request.Builder().url(BenchConstants.BENCH_CREATE_URI).post(requestBody).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody responseBody=response.body()){
                    if(responseBody==null)
                        throw new IllegalStateException("ResponseBody is null");
                    String responseStr=responseBody.string();
                    Bench bench=gson.fromJson(responseStr,Bench.class);
                    callback.success(bench);
                }catch (Exception e){
                    callback.fail(e);
                }
            }
        });
    }

}
