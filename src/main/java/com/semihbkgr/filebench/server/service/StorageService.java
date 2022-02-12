package com.semihbkgr.filebench.server.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageService {

    Mono<Void> saveFile(String dirname, String filename, FilePart filePart);

    Flux<DataBuffer> getFile(String dirname, String filename);

    Mono<Void> deleteFile(String dirname, String filename);

    Mono<Void> deleteBench(String dirname);

}
