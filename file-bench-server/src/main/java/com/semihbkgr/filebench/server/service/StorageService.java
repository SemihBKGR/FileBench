package com.semihbkgr.filebench.server.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageService {

    Mono<Void> saveFile(String benchId,String fileId,Mono<FilePart> filePartMono);

    Mono<Void> updateFile(String benchId,String fileId,Mono<FilePart> filePartMono);

    Flux<DataBuffer> getFile(String benchId,String fileId);

    Mono<Void> deleteBench(String benchId);

    Mono<Void> deleteFile(String benchId,String fileId);

}
