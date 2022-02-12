package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.File;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<File> save(File file);

    Flux<File> findAllByBenchId(int id);

    Mono<Void> deleteById(int id);

}
