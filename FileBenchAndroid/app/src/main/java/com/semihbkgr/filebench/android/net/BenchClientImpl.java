package com.semihbkgr.filebench.android.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.dto.BenchCreateDto;
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
        Request request = new Request.Builder().url(BenchConstants.BENCH_GET_URI).get().build();
        enqueueBenchRequest(request,callback);
    }

    @Override
    public void createBench(@NonNull BenchCreateDto benchCreateDto, @NonNull ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(benchCreateDto));
        Request request = new Request.Builder().url(BenchConstants.BENCH_CREATE_URI).post(requestBody).build();
        enqueueBenchRequest(request,callback);
    }

    private void enqueueBenchRequest(@NonNull Request request, @NonNull ClientCallback<? super Bench> callback){
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,@NonNull IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null)
                        throw new IllegalStateException("ResponseBody is null");
                    String responseJsonStr=responseBody.string();
                    Optional<Bench> benchOptional = unmarshallBench(responseJsonStr);
                    if (benchOptional.isPresent())
                        callback.success(benchOptional.get());
                    else {
                        Optional<ErrorModel> errorModelOptional = unmarshallErrorModel(responseJsonStr);
                        if (errorModelOptional.isPresent())
                            callback.error(errorModelOptional.get());
                        else
                            throw new IllegalStateException("Response cannot be unmarshalled successfully");
                    }
                }
            }
        });
    }

    @NonNull
    private Optional<Bench> unmarshallBench(@Nullable String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty())
            return Optional.empty();
        try {
            return Optional.of(gson.fromJson(jsonStr, Bench.class));
        } catch (JsonSyntaxException ignore) {
            return Optional.empty();
        }
    }

    @NonNull
    private Optional<ErrorModel> unmarshallErrorModel(@Nullable String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty())
            return Optional.empty();
        try {
            return Optional.of(gson.fromJson(jsonStr, ErrorModel.class));
        } catch (JsonSyntaxException ignore) {
            return Optional.empty();
        }
    }

}
