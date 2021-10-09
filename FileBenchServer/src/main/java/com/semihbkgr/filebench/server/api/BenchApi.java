package com.semihbkgr.filebench.server.api;

import com.semihbkgr.filebench.server.component.NumericalIdGenerator;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.model.dto.BenchCreateDto;
import com.semihbkgr.filebench.server.model.dto.FileCreateDto;
import com.semihbkgr.filebench.server.service.BenchService;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final NumericalIdGenerator idGenerator;
    private final StorageService storageService;

    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bench> createBench(@RequestBody BenchCreateDto benchCreateDto) {
        return benchService.save(benchOf(benchCreateDto));
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Bench> getBench(@PathVariable("id") String id) {
        return benchService.findById(id);
    }


    @PostMapping(path = "/file/{bench_id}", consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bench> createFile(@RequestBody FileCreateDto fileCreateDto,
                                  @PathVariable("bench_id") String benchId) {
        return benchService.findById(benchId)
                .doOnNext(bench -> {
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                    bench.getFiles().add(fileOf(fileCreateDto));
                })
                .flatMap(bench -> benchService.update(benchId, bench));
    }

    
    @PostMapping("/update/{bench_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono,
                                 @PathVariable("bench_id") String benchId,
                                 @PathVariable("file_id") String fileId,
                                 @RequestHeader("Content-Length") long contentLength) {
        return benchService.findById(benchId)
                .doOnNext(bench -> {
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                })
                .flatMap(bench -> {
                    var fileOptional = bench.getFiles()
                            .stream()
                            .filter(file -> file.getId().equals(fileId))
                            .limit(1)
                            .findFirst();
                    if (fileOptional.isPresent()) {
                        var file = fileOptional.get();
                        file.setSize(contentLength);
                        return storageService
                                .saveFile(benchId, fileId, filePartMono)
                                .then(benchService.update(benchId,bench))
                                .then(Mono.just(file));
                    } else {
                        return Mono.error(new IllegalArgumentException());
                    }
                });
    }

    @GetMapping(value = "/r/{bench_id}/{file_id}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getResource(@PathVariable("bench_id") String benchId, @PathVariable("file_id") String fileId) {
        return storageService.getFile(benchId, fileId);
    }

    private Bench benchOf(@NonNull BenchCreateDto benchCreateDto) {
        long currentTimeMs = System.currentTimeMillis();
        return Bench
                .builder()
                .id(idGenerator.generate())
                .name(benchCreateDto.getName())
                .description(benchCreateDto.getDescription())
                .files(Collections.emptyList())
                .creationTimeMs(currentTimeMs)
                .expirationTimeMs(currentTimeMs + benchCreateDto.getExpirationDurationMs())
                .viewCount(0L)
                .build();
    }

    private File fileOf(@NonNull FileCreateDto fileCreateDto) {
        return File
                .builder()
                .id(idGenerator.generate())
                .name(fileCreateDto.getName())
                .label(fileCreateDto.getLabel())
                .description(fileCreateDto.getDescription())
                .build();
    }

}
