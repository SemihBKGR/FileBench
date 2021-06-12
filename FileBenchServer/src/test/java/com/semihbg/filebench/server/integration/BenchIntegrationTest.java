package com.semihbg.filebench.server.integration;

import com.semihbg.filebench.server.api.BenchApi;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.repository.BenchRepository;
import com.semihbg.filebench.server.service.BenchServiceImpl;
import com.semihbg.filebench.server.util.BenchUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static com.semihbg.filebench.server.util.BenchUtils.*;
import static com.semihbg.filebench.server.util.BenchUtils.defaultBench;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BenchApi.class)
@Import(BenchServiceImpl.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BenchIntegrationTest {

    @MockBean
    BenchRepository benchRepository;

    @Autowired
    private WebTestClient webClient;

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
    void createBench(){
        Bench bench= defaultBench();
        webClient.post()
                .uri("/bench/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bench))
                .exchange()
                .expectStatus().isCreated();
    }


}
