package com.semihbkgr.filebench.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.semihbkgr.filebench.server.config.BenchConfig;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.service.BenchService;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j(topic = "bench")
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchController {

    private final BenchService benchService;
    private final StorageService storageService;
    private final BenchConfig.BenchProperties benchProperties;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Bench.Views.BenchSecrets.class)
    public Mono<Bench> createBench(@RequestBody Bench bench) {
        if (bench.getExpirationDuration() < benchProperties.getMinExpirationDuration())
            bench.setExpirationDuration(benchProperties.getMinExpirationDuration());
        else if (bench.getExpirationDuration() > benchProperties.getMaxExpirationDuration())
            bench.setExpirationDuration(benchProperties.getMaxExpirationDuration());
        return benchService.saveBench(bench);
    }

    @GetMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Bench.Views.BenchDetails.class)
    public Mono<Bench> getBench(@PathVariable("bench_id") int benchId, @RequestParam(value = "access_token") String accessToken) {
        return benchService.getBench(benchId, accessToken)
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @PutMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Bench> updateBench(@RequestBody Bench bench,
                                   @PathVariable("bench_id") int benchId, @RequestParam(value = "edit_token") String editToken) {
        return benchService.updateBench(benchId, editToken, bench)
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @DeleteMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> deleteBench(@PathVariable("bench_id") int benchId, @RequestParam(value = "edit_token") String editToken) {
        return benchService.deleteBench(benchId, editToken)
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @PostMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> createFile(@PathVariable("bench_id") int benchId, @RequestParam(value = "edit_token") String editToken,
                                 @RequestPart(value = "name", required = false) String name, @RequestPart(value = "description", required = false) String description,
                                 @RequestPart("content") Mono<FilePart> filePartMono, @RequestHeader("content-length") long contentLength) {
        if (contentLength > benchProperties.getFile().getMaxSize())
            return Mono.error(new IllegalArgumentException("A file size can be max " + benchProperties.getFile().getMaxSize() + " bytes"));
        return filePartMono.flatMap(filePart -> {
            var file = new File();
            file.setName(name);
            file.setDescription(description);
            file.setSize(contentLength);
            return benchService.addFile(benchId, editToken, file, bench -> {
                        if (bench.getFileCount() >= benchProperties.getMaxFileCount())
                            return Optional.of(new IllegalStateException("A bench can have max " + benchProperties.getMaxFileCount() + " files"));
                        if (bench.getFileSize() + contentLength > benchProperties.getMaxFileSize())
                            return Optional.of(new IllegalStateException("A bench size can be max " + benchProperties.getMaxFileSize() + " bytes"));
                        return Optional.empty();
                    })
                    .flatMap(benchFileTuple ->
                            storageService.saveFile(benchFileTuple.getT1().getDirname(), benchFileTuple.getT2().getFilename(), filePart)
                                    .thenReturn(benchFileTuple.getT2()))
                    .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                    .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()))
                    .onErrorMap(IOException.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        });
    }

    @GetMapping(value = "/{bench_id}/{file_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getFile(@PathVariable("bench_id") int benchId, @PathVariable("file_id") int fileId, @RequestParam("access_token") String accessToken) {
        return benchService.getFile(benchId, accessToken, fileId)
                .flatMapMany(benchfileTuple -> storageService.getFile(benchfileTuple.getT1().getDirname(), benchfileTuple.getT2().getFilename()))
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()))
                .onErrorMap(NoSuchFileException.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
                .onErrorMap(IOException.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @PutMapping(value = "/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> updateFile(@PathVariable("bench_id") int benchId, @PathVariable("file_id") int fileId,
                                 @RequestParam("edit_token") String editToken, @RequestBody File file) {
        return benchService.updateFile(benchId, editToken, fileId, file)
                .map(Tuple2::getT2)
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @DeleteMapping(value = "/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> deleteFile(@PathVariable("bench_id") int benchId, @PathVariable("file_id") int fileId,
                                 @RequestParam("edit_token") String editToken) {
        return benchService.removeFile(benchId, editToken, fileId)
                .flatMap(benchFileTuple ->
                        storageService.deleteFile(benchFileTuple.getT1().getDirname(), benchFileTuple.getT2().getFilename())
                                .thenReturn(benchFileTuple.getT2()))
                .onErrorMap(NoSuchElementException.class, e -> new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorMap(IllegalArgumentException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()))
                .onErrorMap(NoSuchFileException.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
                .onErrorMap(IOException.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
