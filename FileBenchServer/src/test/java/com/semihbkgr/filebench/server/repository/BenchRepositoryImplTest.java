package com.semihbkgr.filebench.server.repository;

import com.semihbkgr.filebench.server.model.Bench;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class BenchRepositoryImplTest {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    private BenchRepositoryImpl benchRepository;

    @BeforeEach
    void createRequiredObjects(){
        this.benchRepository=new BenchRepositoryImpl(mongoTemplate);
    }

    @Test
    void save() {
        var bench= Bench.builder()
                .id("id")
                .token("token")
                .name("name")
                .description("description")
                .expirationDurationMs(0L)
                .creationTimeMs(0L)
                .viewCount(0L)
                .build();
        var benchMono=benchRepository.save(bench).log();
        StepVerifier.create(benchMono)
                .expectNext(bench)
                .verifyComplete();
    }

    @Test
    void findById() {
    }

    @Test
    void findAllInfo() {
    }

    @Test
    void deleteById() {
    }
}