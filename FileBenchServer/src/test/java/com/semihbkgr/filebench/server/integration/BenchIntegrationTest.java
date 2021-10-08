package com.semihbkgr.filebench.server.integration;

import com.semihbkgr.filebench.server.api.BenchApi;
import com.semihbkgr.filebench.server.component.NumericalIdGenerator;
import com.semihbkgr.filebench.server.model.dto.BenchCreateDto;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.service.BenchServiceImpl;
import com.semihbkgr.filebench.server.util.BenchUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.semihbkgr.filebench.server.util.BenchUtils.defaultBench;
import static com.semihbkgr.filebench.server.util.BenchUtils.defaultBenchCreateDto;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BenchApi.class)
@Import({BenchServiceImpl.class, NumericalIdGenerator.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("ConstantConditions")
public class BenchIntegrationTest {

    @MockBean
    BenchRepository benchRepository;

    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void initialize() {
        log.info("Mock objects initialization is started");
        Mockito.when(benchRepository.save(ArgumentMatchers.any()))
                .thenReturn(Mono.just(defaultBench()));
        Mockito.when(benchRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(defaultBench()));
        log.info("Mock objects initialization is done");
    }

    @Test
    @DisplayName("Create Bench")
    void createBench() {
        BenchCreateDto benchCreateDto = defaultBenchCreateDto();
        webClient.post()
                .uri("/bench/create")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(Mono.just(benchCreateDto), BenchCreateDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Bench.class)
                .isEqualTo(BenchUtils.defaultBench());
        Mockito.verify(benchRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Get Bench")
    void getBench(){
        String id="0000000";
        webClient.get()
                .uri("/bench/get/".concat(id))
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Bench.class)
                .isEqualTo(BenchUtils.defaultBench());
        Mockito.verify(benchRepository, Mockito.times(1)).findById(id);
    }

}
