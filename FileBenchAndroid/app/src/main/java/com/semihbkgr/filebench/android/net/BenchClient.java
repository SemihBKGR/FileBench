package com.semihbkgr.filebench.android.net;

import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.dto.BenchCreateDto;

public interface BenchClient {

    void getBench(String benchId,ClientCallback<? super Bench> callback);

    void createBench(BenchCreateDto benchCreateDto,ClientCallback<? super Bench> callback);

}
