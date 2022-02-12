package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.File;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<File, Integer> {

    Mono<File> findByIdAndBenchId(int id, int benchId);

    Flux<File> findAllByBenchId(int benchId);

}