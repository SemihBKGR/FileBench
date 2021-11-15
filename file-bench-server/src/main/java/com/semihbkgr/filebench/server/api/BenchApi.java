package com.semihbkgr.filebench.server.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.semihbkgr.filebench.server.component.NumericalIdGenerator;
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

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j(topic = "bench")
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final NumericalIdGenerator idGenerator;
    private final StorageService storageService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Bench.Views.BenchSecrets.class)
    public Mono<Bench> createBench(@RequestBody Bench bench) {
        bench.setId(idGenerator.generate());
        bench.setToken(idGenerator.generate());
        bench.setCreationTimeMs(System.currentTimeMillis());
        return benchService.save(bench)
                .doOnNext(savedBench -> log.info("Bench | create - id: {}", bench.getId()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Bench.Views.BenchDetails.class)
    public Mono<Bench> getBench(@PathVariable("id") String id) {
        return benchService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")));
    }

    @PutMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Bench> updateBench(@RequestBody Bench bench,
                                   @PathVariable("bench_id") String benchId,
                                   @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(benchFromDb -> {
                    if (benchFromDb.getToken().equals(token)) {
                        benchFromDb.setName(bench.getName());
                        return benchService.save(benchFromDb);
                    } else
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                })
                .doOnNext(updatedBench -> log.info("Bench | update - id: {}", benchId));
    }

    @DeleteMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Bench> deleteBench(@PathVariable("bench_id") String benchId,
                                   @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    return benchService.deleteById(benchId)
                            .then(storageService.deleteBench(benchId))
                            .thenReturn(bench);
                })
                .doOnNext(updatedBench -> log.info("Bench | delete - id: {}", benchId));
    }

    @PostMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> createFile(@PathVariable("bench_id") String benchId,
                                 @RequestParam("token") String token,
                                 @RequestPart("content") Mono<FilePart> filePartMono,
                                 @RequestHeader("Content-Length") long contentLength) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                    var file = new File();
                    file.setId(idGenerator.generate());
                    file.setSize(contentLength);
                    return storageService.saveFile(benchId, file.getId(), filePartMono)
                            .flatMap(filename -> {
                                file.setName(filename);
                                bench.getFiles().add(file);
                                return benchService.save(bench);
                            })
                            .thenReturn(file);
                })
                .doOnNext(file -> log.info("File | create - benchId: {}, fileId: {}", benchId, file.getId()));
    }

    @GetMapping(value = "/{bench_id}/{file_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getFile(@PathVariable("bench_id") String benchId,
                                    @PathVariable("file_id") String fileId) {
        return storageService.getFile(benchId, fileId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found")));
    }

    @DeleteMapping("/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> deleteFile(@PathVariable("bench_id") String benchId,
                                 @PathVariable("file_id") String fileId,
                                 @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                    File deletedFile = null;
                    var itr = bench.getFiles().iterator();
                    while (itr.hasNext()) {
                        var f = itr.next();
                        if (f.getId().equals(fileId)) {
                            itr.remove();
                            deletedFile = f;
                        }
                    }
                    if (deletedFile == null)
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    return benchService.save(bench)
                            .then(storageService.deleteFile(benchId, fileId))
                            .thenReturn(deletedFile);
                })
                .doOnNext(file -> log.info("File | delete - benchId: {}, fileId: {}", benchId, file.getId()));
    }

}
