package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BenchRepository extends R2dbcRepository<Bench, Integer> {

    Mono<Bench> findById(int id);

    @Query(value = "SELECT `id`,`dirname` FROM benches WHERE `creation_time` + `expiration_duration` > CURRENT_DATE()")
    Flux<BenchInfo> findAllExpiredBenchInfos();

}
