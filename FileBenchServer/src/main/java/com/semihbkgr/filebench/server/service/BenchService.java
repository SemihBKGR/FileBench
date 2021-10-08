package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import reactor.core.publisher.Mono;

public interface BenchService {

    Mono<Bench> save(Bench bench);

    Mono<Bench> update(String id,Bench bench);

    Mono<Bench> findById(String id);

    Mono<Void> deleteById(String id);

}
