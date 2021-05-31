package com.smh.bs.server.repository.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BundleUploadTraceRepositoryImpl implements BundleUploadTraceRepository {

    private static final String KEY = "bundle-trace";
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    public BundleUploadTraceRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(BundleUploadTrace bundleUploadTrace) {
        hashOperations.put(KEY, bundleUploadTrace.getBundleId(), bundleUploadTrace);
    }

    @Override
    public void update(BundleUploadTrace bundleUploadTrace) {
        delete(bundleUploadTrace.getBundleId());
        hashOperations.put(KEY, bundleUploadTrace.getBundleId(), bundleUploadTrace);
    }

    @Override
    public BundleUploadTrace find(String bundleId) {
        Object o =hashOperations.get(KEY,bundleId);
        if(o instanceof BundleUploadTrace) return (BundleUploadTrace) o;
        else throw new IllegalArgumentException();
    }

    @Override
    public void delete(String bundleId) {
        long deletedRowCount = hashOperations.delete(KEY, bundleId);
        if (deletedRowCount == 0) throw new IllegalStateException();
    }

}
