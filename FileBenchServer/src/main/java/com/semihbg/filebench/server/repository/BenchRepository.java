package com.semihbg.filebench.server.repository;


import com.semihbg.filebench.server.model.Bench;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BenchRepository extends ReactiveMongoRepository<Bench,String> {

}
