package com.semihbkgr.filebench.android.net;

import com.semihbkgr.filebench.android.model.Bench;

public interface BenchClient {

    void getBench(String benchId, ClientCallback<? super Bench> callback);

    void createBench(Bench bench, ClientCallback<? super Bench> callback);

    void uploadFile(String benchId, String token, String name, byte[] content, ClientCallback<? super Bench> callback);

    void updateBench(String benchId, String token, Bench bench, ClientCallback<? super Bench> callback);

}
