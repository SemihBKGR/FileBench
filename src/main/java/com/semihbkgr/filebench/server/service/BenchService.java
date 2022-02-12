package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.File;
import org.springframework.cglib.core.internal.Function;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Optional;

public interface BenchService {

    Mono<Bench> saveBench(Bench bench);

    Mono<Bench> updateBench(int benchId, String editToken, Bench bench);

    Mono<Bench> getBench(int benchId, String accessToken);

    Mono<Void> deleteBench(int benchId, String editToken);

    Mono<Tuple2<Bench, File>> getFile(int benchId, String accessToken, int fileId);

    Mono<Tuple2<Bench, File>> addFile(int benchId, String editToken, File file, Function<? super Bench, Optional<? extends Throwable>> function);

    Mono<Tuple2<Bench, File>> updateFile(int benchId, String editToken, int fileId, File file);

    Mono<Tuple2<Bench, File>> removeFile(int benchId, String editToken, int fileId);

    Mono<Long> allSize();

}
