package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BenchRepositoryImpl implements BenchRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Bench> save(Bench bench) {
        return mongoTemplate.insert(bench);
    }

    @Override
    public Mono<Bench> findById(String id) {
        return mongoTemplate.findById(id,Bench.class);
    }

    @Override
    public Flux<BenchInfo> findAllInfo() {
        return mongoTemplate.findAll(BenchInfo.class);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)),Bench.class).then();
    }

}
