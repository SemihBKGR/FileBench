package com.semihbkgr.filebench.server.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.semihbkgr.filebench.server.component.NumericalIdGenerator;
import com.semihbkgr.filebench.server.component.TokenGenerator;
import com.semihbkgr.filebench.server.error.IncorrectTokenException;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.model.dto.BenchCreateDto;
import com.semihbkgr.filebench.server.model.dto.BenchUpdateDto;
import com.semihbkgr.filebench.server.model.dto.FileUpdateDto;
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
    private final TokenGenerator tokenGenerator;

    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Bench.Views.BenchWriteAccess.class)
    public Mono<Bench> createBench(@RequestBody BenchCreateDto benchCreateDto) {
        return benchService.save(benchOf(benchCreateDto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Bench.Views.BenchReadAccess.class)
    public Mono<Bench> getBench(@PathVariable("id") String id) {
        return benchService.findById(id);
    }

    @PostMapping("/c/{bench_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> createFile(@PathVariable("bench_id") String benchId,
                                 @RequestPart("content") Mono<FilePart> filePartMono,
                                 @RequestParam("token") String token,
                                 @RequestHeader("Content-Length") long contentLength) {
        return benchService.findById(benchId)
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new IncorrectTokenException(HttpStatus.FORBIDDEN, "Token is incorrect"));
                    var file = fileOf(contentLength);
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                    bench.getFiles().add(file);
                    return storageService.saveFile(benchId, file.getId(), filePartMono)
                            .then(benchService.update(benchId, bench))
                            .thenReturn(file);
                });
    }

    @GetMapping("/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<File> getFile(@PathVariable("bench_id") String benchId,
                              @PathVariable("file_id") String fileId) {
        return benchService.findById(benchId)
                .flatMap(bench -> {
                    if (bench.getFiles() == null)
                        return Mono.error(new IllegalArgumentException());
                    var fileOptional = bench.getFiles().stream()
                            .filter(file -> file.getId().equals(fileId))
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new IllegalArgumentException());
                    return Mono.just(fileOptional.get());
                });
    }

    @GetMapping(value = "/c/{bench_id}/{file_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getFileContent(@PathVariable("bench_id") String benchId,
                                           @PathVariable("file_id") String fileId) {
        return storageService.getFile(benchId, fileId);
    }

    @PostMapping("/c/u/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> updateFileContent(@PathVariable("bench_id") String benchId,
                                        @PathVariable("file_id") String fileId,
                                        @RequestPart("content") Mono<FilePart> filePartMono,
                                        @RequestParam("token") String token,
                                        @RequestHeader("Content-Length") long contentLength) {
        return benchService.findById(benchId)
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new IncorrectTokenException(HttpStatus.FORBIDDEN, "Token is incorrect"));
                    if (bench.getFiles() == null)
                        return Mono.error(new IllegalArgumentException());
                    var fileOptional = bench.getFiles()
                            .stream()
                            .filter(file -> file.getId().equals(fileId))
                            .limit(1)
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new IllegalArgumentException());
                    var file = fileOptional.get();
                    file.setSize(contentLength);
                    return storageService
                            .updateFile(benchId, fileId, filePartMono)
                            .then(benchService.update(benchId, bench))
                            .then(Mono.just(file));
                });
    }

    @PostMapping("/u/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @JsonView(Bench.Views.BenchWriteAccess.class)
    public Mono<Bench> updateBenchProperties(@RequestBody BenchUpdateDto benchUpdateDto,
                                             @PathVariable("bench_id") String benchId,
                                             @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new IncorrectTokenException(HttpStatus.FORBIDDEN, "Token is incorrect"));
                    bench.setName(benchUpdateDto.getName());
                    bench.setDescription(benchUpdateDto.getDescription());
                    return benchService.update(benchId, bench);
                });
    }

    @PostMapping("/u/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> updateFileProperties(@RequestBody FileUpdateDto fileUpdateDto,
                                           @PathVariable("bench_id") String benchId,
                                           @PathVariable("file_id") String fileId,
                                           @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new IncorrectTokenException(HttpStatus.FORBIDDEN, "Token is incorrect"));
                    if (bench.getFiles() == null)
                        return Mono.error(new IllegalArgumentException());
                    var fileOptional = bench.getFiles()
                            .stream()
                            .filter(file -> file.getId().equals(fileId))
                            .limit(1)
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new IllegalArgumentException());
                    var file = fileOptional.get();
                    file.setName(fileUpdateDto.getName());
                    file.setDescription(fileUpdateDto.getDescription());
                    file.setLabel(fileUpdateDto.getLabel());
                    return benchService.update(benchId, bench)
                            .thenReturn(file);
                });
    }

    private Bench benchOf(@NonNull BenchCreateDto benchCreateDto) {
        long currentTimeMs = System.currentTimeMillis();
        return Bench.builder()
                .id(idGenerator.generate())
                .token(tokenGenerator.generate())
                .name(benchCreateDto.getName())
                .description(benchCreateDto.getDescription())
                .files(Collections.emptyList())
                .creationTimeMs(currentTimeMs)
                .expirationTimeMs(currentTimeMs + benchCreateDto.getExpirationDurationMs())
                .viewCount(0L)
                .build();
    }

    private File fileOf(long size) {
        return File.builder()
                .id(idGenerator.generate())
                .name(null)
                .description(null)
                .label(null)
                .size(size)
                .downloadCount(0L)
                .build();
    }

}
