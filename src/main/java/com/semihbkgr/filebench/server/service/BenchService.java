package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BenchService {

    Mono<Bench> save(Bench bench);

    Flux<BenchInfo> findAll();

    Mono<Bench> findById(String id);

    Mono<Void> deleteById(String id);

}
