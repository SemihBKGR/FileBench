package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BenchRepository {

    Mono<Bench> save(Bench bench);

    Mono<Bench> findById(String id);

    Flux<BenchInfo> findAllInfo();

    Mono<Void> deleteById(String id);

}
