package com.semihbg.filebench.server.service;

import com.semihbg.filebench.server.model.Bench;
import reactor.core.publisher.Mono;

public interface BenchService {

    Mono<Bench> save(Bench bench);

    Mono<Bench> findById(String id);

    Mono<Void> deleteById(String id);

}