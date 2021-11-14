package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.dto.BenchInfoDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BenchRepository extends ReactiveMongoRepository<Bench, String> {

    Flux<BenchInfoDto> findAllBy();

}
