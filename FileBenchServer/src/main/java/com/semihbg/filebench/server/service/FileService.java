package com.semihbg.filebench.server.service;

import com.semihbg.filebench.server.model.Bench;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<Bench> save(Bench bench,Flux<FilePart> filePartFlux);

    void delete(Bench bench);

}
