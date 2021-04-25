package com.smh.bs.server.repository.redis;

public interface BundleUploadTraceRepository {

    void save(BundleUploadTrace bundleUploadTrace);
    void update(BundleUploadTrace bundleUploadTrace);
    BundleUploadTrace find(String bundleId);
    void delete(String bundleId);

}
