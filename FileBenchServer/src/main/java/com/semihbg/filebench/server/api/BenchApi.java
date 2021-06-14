package com.semihbg.filebench.server.api;

import com.semihbg.filebench.server.component.BenchIdGenerator;
import com.semihbg.filebench.server.dto.BenchCreateDto;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.service.BenchService;
import com.semihbg.filebench.server.validation.BenchValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final BenchIdGenerator idGenerator;
    private final BenchValidator benchValidator;

    @PostMapping(value = "/create", consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bench> create(@RequestBody BenchCreateDto benchCreateDto) {
        Bench bench = Bench.of(benchCreateDto);
        bench.setId(idGenerator.generate());
        bench.setFiles(Collections.emptyList());
        bench.setCreatedTime(System.currentTimeMillis());
        bench.setViewCount(0);
        benchValidator.validateAndThrowIfInvalid(bench);
        return benchService.save(bench);
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Bench> getBenchById(@PathVariable("id") String id) {
        return benchService.findById(id);
    }

}
