package com.semihbg.filebench.server.service;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.repository.BenchRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.semihbg.filebench.server.util.BenchUtils.defaultBench;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BenchServiceImplTest {

    @Mock
    BenchRepository benchRepository;

    @InjectMocks
    BenchServiceImpl benchService;

    @BeforeEach
    void initialize() {
        log.info("Mock objects initialization is started");
        Mockito.when(benchRepository.save(defaultBench()))
                .thenReturn(Mono.just(defaultBench()));
        Mockito.when(benchRepository.save(null))
                .thenReturn(Mono.error(new IllegalArgumentException("Bench cannot be null")));
        log.info("Mock objects initialization is done");
    }

    @Test
    @DisplayName("Save Default Bench")
    void saveDefaultBench() {
        Bench bench = defaultBench();
        Mono<Bench> benchMono = benchService.save(bench);
        StepVerifier.create(benchMono)
                .expectNext(bench)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Save Null Bench")
    void saveNullBench() {
        Mono<Bench> benchMono = benchService.save(null);
        StepVerifier.create(benchMono)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

}