package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.component.NameGenerator;
import com.semihbkgr.filebench.server.component.TokenGenerator;
import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.repository.FileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BenchServiceImpl implements BenchService {

    private final BenchRepository benchRepository;
    private final FileRepository fileRepository;
    private final TokenGenerator tokenGenerator;
    private final NameGenerator nameGenerator;

    @Override
    public Mono<Bench> saveBench(@NonNull Bench bench) {
        var recordBench = bench.withId(null);
        recordBench.setFileCount(0);
        recordBench.setFileSize(0L);
        recordBench.setViewCount(0L);
        recordBench.setAccessToken(tokenGenerator.token());
        recordBench.setEditToken(tokenGenerator.token());
        recordBench.setDirname(nameGenerator.name());
        recordBench.setCreationTime(System.currentTimeMillis());
        return benchRepository.save(recordBench);
    }

    @Override
    public Mono<Bench> updateBench(int benchId, String editToken, Bench bench) {
        return getBenchAndCheckEditToken(benchId, editToken)
                .flatMap(benchFromDB -> {
                    benchFromDB.setName(bench.getName());
                    benchFromDB.setDescription(bench.getDescription());
                    return benchRepository.save(benchFromDB);
                });
    }

    @Override
    public Mono<Bench> getBench(int benchId, String accessToken) {
        return getBenchAndCheckAccessToken(benchId, accessToken)
                .flatMap(benchFromDB -> fileRepository.findAllByBenchId(benchId)
                        .collectList()
                        .map(files -> {
                            benchFromDB.setFiles(files);
                            return benchFromDB;
                        }));
    }

    @Override
    public Mono<Bench> deleteBench(int benchId, String editToken) {
        return getBenchAndCheckEditToken(benchId, editToken)
                .flatMap(benchFromDB ->
                        benchRepository.delete(benchFromDB)
                                .thenReturn(benchFromDB));
    }

    @Override
    public Mono<Tuple2<Bench, File>> getFile(int benchId, String accessToken, int fileId) {
        return getBenchAndCheckAccessToken(benchId, accessToken)
                .flatMap(benchFromDB ->
                        fileRepository.findByIdAndBenchId(fileId, benchId)
                                .switchIfEmpty(Mono.error(new NoSuchElementException("No such file with given id, id: " + fileId)))
                                .map(fileFromDB -> Tuples.of(benchFromDB, fileFromDB)));
    }

    @Override
    public Mono<Tuple2<Bench, File>> addFile(int benchId, String editToken, File file, Function<? super Bench, Optional<? extends Throwable>> function) {
        var recordFile = file.withId(null);
        recordFile.setBenchId(benchId);
        recordFile.setFilename(nameGenerator.name());
        recordFile.setDownloadCount(0L);
        return getBenchAndCheckEditToken(benchId, editToken)
                .flatMap(benchFromDB -> {
                    var optional = function.apply(benchFromDB);
                    if (optional.isEmpty()) {
                        benchFromDB.setFileCount(benchFromDB.getFileCount() + 1);
                        benchFromDB.setFileSize(benchFromDB.getFileSize() + file.getSize());
                        return benchRepository.save(benchFromDB)
                                .flatMap(savedBenchFromDB ->
                                        fileRepository.save(recordFile)
                                                .flatMap(fileFromDB ->
                                                        Mono.just(Tuples.of(savedBenchFromDB, fileFromDB))));
                    } else
                        return Mono.error(optional.get());
                });
    }

    @Override
    public Mono<Tuple2<Bench, File>> updateFile(int benchId, String editToken, int fileId, File file) {
        return getBenchAndCheckEditToken(benchId, editToken)
                .flatMap(benchFromDB ->
                        fileRepository.findByIdAndBenchId(fileId, benchId)
                                .switchIfEmpty(Mono.error(new NoSuchElementException("No such file with given benchId and fileId, benchId: " + benchId + ", fileId: " + fileId)))
                                .flatMap(fileFromDB -> {
                                    fileFromDB.setName(file.getName());
                                    fileFromDB.setDescription(file.getDescription());
                                    return fileRepository.save(fileFromDB)
                                            .map(savedFileFromDB -> Tuples.of(benchFromDB, savedFileFromDB));
                                }));
    }

    @Override
    public Mono<Tuple2<Bench, File>> removeFile(int benchId, String editToken, int fileId) {
        return getBenchAndCheckEditToken(benchId, editToken)
                .flatMap(benchFromDB ->
                        fileRepository.findByIdAndBenchId(fileId, benchId)
                                .switchIfEmpty(Mono.error(new NoSuchElementException("No such file with given benchId and fileId, benchId: " + benchId + ", fileId: " + fileId)))
                                .flatMap(fileFromDB ->
                                        fileRepository.delete(fileFromDB)
                                                .thenReturn(Tuples.of(benchFromDB, fileFromDB))));
    }

    @Override
    public Mono<Long> allSize() {
        return benchRepository.allFilesSize();
    }

    private Mono<Bench> getBenchAndCheckAccessToken(int benchId, String accessToken) {
        return benchRepository.findById(benchId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No such bench with given id, id: " + benchId)))
                .flatMap(benchFromDB ->
                        benchFromDB.getAccessToken().equals(accessToken) ?
                                Mono.just(benchFromDB) :
                                Mono.error(new IllegalArgumentException("Incorrect accessToken, accessToken: " + accessToken)));
    }

    private Mono<Bench> getBenchAndCheckEditToken(int benchId, String editToken) {
        return benchRepository.findById(benchId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No such bench with given id, id: " + benchId)))
                .flatMap(benchFromDB ->
                        benchFromDB.getEditToken().equals(editToken) ?
                                Mono.just(benchFromDB) :
                                Mono.error(new IllegalArgumentException("Incorrect editToken, editToken: " + editToken)));
    }

}
