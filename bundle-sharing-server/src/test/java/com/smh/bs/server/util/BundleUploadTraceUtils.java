package com.smh.bs.server.util;

import com.smh.bs.server.repository.redis.BundleUploadTrace;

public class BundleUploadTraceUtils {

    public static BundleUploadTrace generateRandom(){
        return BundleUploadTrace.builder()
                .bundleId("random")
                .bundleName("name")
                .uploadedSize(1000)
                .size(10000)
                .uploadedResourceCount(2)
                .resourceCount(5)
                .startTime(System.currentTimeMillis())
                .build();
    }

}
