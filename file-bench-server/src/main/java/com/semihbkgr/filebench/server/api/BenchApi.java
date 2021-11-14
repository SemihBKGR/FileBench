package com.semihbkgr.filebench.server.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.semihbkgr.filebench.server.component.ImageScaleComponent;
import com.semihbkgr.filebench.server.component.NumericalIdGenerator;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.model.dto.BenchCreateDto;
import com.semihbkgr.filebench.server.model.dto.BenchUpdateDto;
import com.semihbkgr.filebench.server.model.dto.FileUpdateDto;
import com.semihbkgr.filebench.server.service.BenchService;
import com.semihbkgr.filebench.server.service.StorageService;
import com.semihbkgr.filebench.server.validation.BenchValidator;
import com.semihbkgr.filebench.server.validation.FileValidator;
import com.semihbkgr.filebench.server.validation.ValidationException;
import lombok.NonNull;
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
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j(topic = "bench")
@RestController
@RequestMapping("/bench")
@RequiredArgsConstructor
public class BenchApi {

    private final BenchService benchService;
    private final NumericalIdGenerator idGenerator;
    private final StorageService storageService;
    private final BenchValidator benchValidator;
    private final FileValidator fileValidator;
    private final ImageScaleComponent imageScaleComponent;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Bench.Views.BenchSecrets.class)
    public Mono<Bench> createBench(@RequestBody BenchCreateDto benchCreateDto) {
        log.info("Bench | create - dto: {}", benchCreateDto);
        return Mono.just(benchMonoOf(benchCreateDto))
                .flatMap(bench -> benchValidator.validate(bench)
                        .flatMap(validationResult -> {
                            if (validationResult.isInvalid())
                                return Mono.error(new ValidationException(validationResult));
                            return benchService.save(bench);
                        }));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Bench.Views.BenchDetails.class)
    public Mono<Bench> getBench(@PathVariable("id") String id) {
        log.debug("Bench | get - id: {}", id);
        return benchService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")));
    }

    @PutMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Bench> updateBenchProperties(@RequestBody BenchUpdateDto benchUpdateDto,
                                             @PathVariable("bench_id") String benchId,
                                             @RequestParam("token") String token) {
        log.debug("Bench | update - benchId: {}, dto: {}", benchId, benchUpdateDto);
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    bench.setName(benchUpdateDto.getName());
                    bench.setDescription(benchUpdateDto.getDescription());
                    return benchValidator.validate(bench)
                            .flatMap(validationResult -> {
                                if (validationResult.isInvalid())
                                    return Mono.error(new ValidationException(validationResult));
                                return benchService.update(benchId, bench);
                            });
                });
    }


    @PostMapping("/{bench_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<File> createFile(@PathVariable("bench_id") String benchId,
                                 @RequestParam("token") String token,
                                 @RequestPart("content") Mono<FilePart> filePartMono,
                                 @RequestPart(name = "description", required = false) String description,
                                 @RequestPart(name = "label", required = false) String label,
                                 @RequestHeader("Content-Length") long contentLength) {
        log.info("File | create - benchId:{}, length: {} ,description: {}, label: {}", benchId, contentLength, description, label);
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    if (bench.getFiles() == null)
                        bench.setFiles(new ArrayList<>());
                    var file = fileOf(contentLength);
                    file.setDescription(description);
                    file.setLabel(label);
                    bench.getFiles().add(file);
                    return filePartMono
                            .doOnNext(filePart -> file.setName(filePart.filename()))
                            .then(fileValidator.validate(file))
                            .flatMap(validationResult -> {
                                if (validationResult.isInvalid())
                                    return Mono.error(new ValidationException(validationResult));
                                return storageService.saveFile(benchId, file.getId(), filePartMono)
                                        .then(benchService.update(benchId, bench))
                                        .thenReturn(file);
                            });
                });
    }

    @GetMapping("/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<File> getFile(@PathVariable("bench_id") String benchId,
                              @PathVariable("file_id") String fileId) {
        log.debug("File | get - benchId: {}, fileId: {}", benchId, fileId);
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (bench.getFiles() == null)
                        return Mono.error(new IllegalArgumentException());
                    var fileOptional = bench.getFiles().stream()
                            .filter(file -> file.getId().equals(fileId))
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    return Mono.just(fileOptional.get());
                });
    }

    @GetMapping(value = "/{bench_id}/content/{file_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getFileContent(@PathVariable("bench_id") String benchId,
                                           @PathVariable("file_id") String fileId) {

        return storageService.getFile(benchId, fileId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File content not found")));
    }


    @PutMapping("/{bench_id}/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> updateFileProperties(@RequestBody FileUpdateDto fileUpdateDto,
                                           @PathVariable("bench_id") String benchId,
                                           @PathVariable("file_id") String fileId,
                                           @RequestParam("token") String token) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    if (bench.getFiles() == null)
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    var fileOptional = bench.getFiles()
                            .stream()
                            .filter(file -> file.getId().equals(fileId))
                            .limit(1)
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    var file = fileOptional.get();
                    file.setName(fileUpdateDto.getName());
                    file.setDescription(fileUpdateDto.getDescription());
                    file.setLabel(fileUpdateDto.getLabel());
                    return fileValidator.validate(file)
                            .flatMap(validationResult -> {
                                if (validationResult.isInvalid())
                                    return Mono.error(new ValidationException(validationResult));
                                return benchService.update(benchId, bench)
                                        .thenReturn(file);
                            });
                });
    }

    @PutMapping("/{bench_id}/content/{file_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<File> updateFileContent(@PathVariable("bench_id") String benchId,
                                        @PathVariable("file_id") String fileId,
                                        @RequestPart("content") Mono<FilePart> filePartMono,
                                        @RequestParam("token") String token,
                                        @RequestHeader("Content-Length") long contentLength) {
        return benchService.findById(benchId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Bench not found")))
                .flatMap(bench -> {
                    if (!bench.getToken().equals(token))
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect token"));
                    if (bench.getFiles() == null)
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    var fileOptional = bench.getFiles()
                            .stream()
                            .filter(file -> file.getId().equals(fileId))
                            .limit(1)
                            .findFirst();
                    if (fileOptional.isEmpty())
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                    var file = fileOptional.get();
                    file.setSize(contentLength);
                    return filePartMono.then(storageService.updateFile(benchId, fileId, filePartMono))
                            .then(benchService.update(benchId, bench))
                            .then(Mono.just(file));
                });
    }

    private Bench benchMonoOf(@NonNull BenchCreateDto benchCreateDto) {
        return Bench.builder()
                .id(idGenerator.generate())
                .token(idGenerator.generate())
                .name(benchCreateDto.getName())
                .description(benchCreateDto.getDescription())
                .files(Collections.emptyList())
                .expirationDurationMs(benchCreateDto.getExpirationDurationMs())
                .creationTimeMs(System.currentTimeMillis())
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
