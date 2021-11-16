package com.semihbkgr.filebench.android.net;

import android.util.Pair;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.semihbkgr.filebench.android.model.Bench;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

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
        enqueueBenchRequest(request, callback);
    }

    @Override
    public void createBench(@NonNull Bench bench, @NonNull ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(bench));
        Request request = new Request.Builder().url(url + "/bench").post(requestBody).build();
        enqueueBenchRequest(request, callback);
    }

    @Override
    public void getFile(String benchId, String fileId, ClientCallback<? super InputStream> callback) {
        Request request = new Request.Builder().url(url + "/bench/" + benchId + "/" + fileId).get().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null)
                        throw new IllegalStateException("ResponseBody is null");
                    if (response.code() / 100 == 2)
                        callback.success(responseBody.byteStream());
                    else
                        callback.error(gson.fromJson(responseBody.string(), ErrorModel.class));
                } catch (Exception e) {
                    callback.fail(e);
                }
            }
        });
    }

    @Override
    public void uploadFile(String benchId, String token, String name, byte[] content, ClientCallback<? super Bench> callback) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", name, RequestBody.create(MediaType.parse("application/octet-stream"), content))
                .build();
        Request request = new Request.Builder().url(url + "/bench/" + benchId + "?token=" + token).post(body).build();
        enqueueBenchRequest(request, callback);
    }

    @Override
    public void uploadFiles(String benchId, String token, Collection<Pair<String, byte[]>> contents, ClientCallback<? super Bench> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        contents.forEach(c -> {
            builder.addFormDataPart("file", c.first, RequestBody.create(MediaType.parse("application/octet-stream"), c.second));
        });
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url + "/bench/" + benchId + "?token=" + token).post(body).build();
        enqueueBenchRequest(request, callback);
    }

    @Override
    public void updateBench(String benchId, String token, Bench bench, ClientCallback<? super Bench> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(bench));
        Request request = new Request.Builder().url(url + "/bench/" + benchId + "?token=" + token).post(requestBody).build();
        enqueueBenchRequest(request, callback);
    }

    private void enqueueBenchRequest(@NonNull Request request, @NonNull ClientCallback<? super Bench> callback) {
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
                } catch (Exception e) {
                    callback.fail(e);
                }
            }
        });
    }


}
