package com.semihbkgr.filebench.server.repository;


import com.semihbkgr.filebench.server.model.Bench;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BenchRepository extends ReactiveMongoRepository<Bench,String> {

}
