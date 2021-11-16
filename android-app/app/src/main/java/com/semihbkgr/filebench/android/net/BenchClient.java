package com.semihbkgr.filebench.android.net;


import android.util.Pair;
import com.semihbkgr.filebench.android.model.Bench;

import java.io.InputStream;
import java.util.Collection;

public interface BenchClient {

    void getBench(String benchId, ClientCallback<? super Bench> callback);

    void createBench(Bench bench, ClientCallback<? super Bench> callback);

    void getFile(String benchId, String fileId, ClientCallback<? super InputStream> callback);

    void uploadFile(String benchId, String token, String name, byte[] content, ClientCallback<? super Bench> callback);

    void uploadFiles(String benchId, String token, Collection<Pair<String, byte[]>> contents, ClientCallback<? super Bench> callback);

    void updateBench(String benchId, String token, Bench bench, ClientCallback<? super Bench> callback);

}
