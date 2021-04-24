package com.smh.bs.server.service;

import com.smh.bs.server.model.Bundle;
import reactor.core.publisher.Mono;

public interface BundleService {

    Mono<Bundle> save(Bundle bundle);
    Mono<Bundle> findById(String id);
    Mono<Void> deleteById(String id);

}
