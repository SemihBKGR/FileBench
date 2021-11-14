package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.dto.BenchInfoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BenchService {

    Mono<Bench> save(Bench bench);

    Mono<Bench> update(String id, Bench bench);

    Flux<BenchInfoDto> findAllInfo();

    Mono<Bench> findById(String id);

    Mono<Void> deleteById(String id);

}
