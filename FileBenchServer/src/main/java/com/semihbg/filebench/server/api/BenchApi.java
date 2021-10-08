package com.semihbg.filebench.server.api;

import com.semihbg.filebench.server.component.NumericalIdGenerator;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import com.semihbg.filebench.server.model.dto.BenchCreateDto;
import com.semihbg.filebench.server.model.dto.FileCreateDto;
import com.semihbg.filebench.server.service.BenchService;
import com.semihbg.filebench.server.validation.BenchValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final NumericalIdGenerator idGenerator;
    private final BenchValidator benchValidator;

    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bench> createBench(@RequestBody BenchCreateDto benchCreateDto) {
        return benchService.save(benchOf(benchCreateDto));
    }

    @PostMapping(path = "/file/{bench_id}", consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bench> createFile(@RequestBody FileCreateDto fileCreateDto,
                                  @PathVariable("bench_id") String benchId){
        return benchService.findById(benchId)
                .doOnNext(bench -> bench.getFiles().add(fileOf(fileCreateDto)))
                .flatMap(bench -> benchService.update(benchId,bench));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Bench> getBenchById(@PathVariable("id") String id) {
        return benchService.findById(id);
    }

    private Bench benchOf(@NonNull BenchCreateDto benchCreateDto) {
        long currentTimeMs = System.currentTimeMillis();
        return Bench
                .builder()
                .id(idGenerator.generate())
                .name(benchCreateDto.getName())
                .description(benchCreateDto.getDescription())
                .creationTimeMs(currentTimeMs)
                .expirationTimeMs(currentTimeMs + benchCreateDto.getExpirationDurationMs())
                .build();
    }

    private File fileOf(@NonNull FileCreateDto fileCreateDto) {
        return File
                .builder()
                .name(fileCreateDto.getName())
                .label(fileCreateDto.getLabel())
                .description(fileCreateDto.getDescription())
                .build();
    }

}
