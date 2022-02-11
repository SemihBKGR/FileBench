package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.File;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface FileRepository extends R2dbcRepository<File, Integer> {

    Flux<File> findAllByBenchId(int benchId);

}