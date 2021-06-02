package com.semihbg.filebench.server.api;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.service.BenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;

    @PostMapping(value = "/create", consumes = {MULTIPART_FORM_DATA_VALUE})
    public Mono<Bench> create(@RequestPart("files") Flux<FilePart> filePartFlux) {
        return filePartFlux.map(file -> {

        });
    }

    @GetMapping("/get/{id}")
    public Mono<Bench> getBenchById(@PathVariable("id") String id) {
        return benchService.findById(id);
    }


}
