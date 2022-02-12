package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BenchService {

    Mono<Bench> save(Bench bench);

    Mono<Bench> findByAccessToken(String token);

    Mono<Bench> findByEditToken(String token);

    Flux<BenchInfo> findAllInfos();

    Mono<Void> deleteById(int id);

}
