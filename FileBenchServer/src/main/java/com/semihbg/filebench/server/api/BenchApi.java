package com.semihbg.filebench.server.api;

import com.semihbg.filebench.server.component.BenchIdGenerator;
import com.semihbg.filebench.server.dto.BenchCreateDto;
import com.semihbg.filebench.server.file.FileContext;
import com.semihbg.filebench.server.file.FileSource;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.service.BenchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final BenchIdGenerator idGenerator;
    private final FileSource fileSource;

    @PostMapping(value = "/create", consumes = {APPLICATION_JSON_VALUE})
    public Mono<Bench> create(@RequestBody BenchCreateDto benchCreateDto) throws IOException {
        Bench bench=Bench.of(benchCreateDto);
        bench.setId(idGenerator.generate());
        bench.setFiles(Collections.emptyList());
        bench.setCreatedTime(System.currentTimeMillis());
        bench.setViewCount(0);
        return benchService.save(bench);
    }

    @GetMapping("/get/{id}")
    public Mono<Bench> getBenchById(@PathVariable("id") String id) {
        return benchService.findById(id);
    }


}
