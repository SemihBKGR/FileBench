package com.semihbg.filebench.server.service;

import com.semihbg.filebench.server.error.FileServiceException;
import com.semihbg.filebench.server.model.Bench;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path rootPath;

    @SneakyThrows(IOException.class)
    public FileServiceImpl(@Value("${file-service.root-path:${user.dir}}")String rootPathString){
        this.rootPath=Path.of(rootPathString);
        if(!Files.exists(rootPath)){
            log.info("FileService RootPath is not exists, RootPath : {}",rootPathString);
            Files.createDirectory(rootPath);
            log.info("FileService RootPath has been created, RootPath : {}",rootPathString);
        }else
            log.info("FileService RootPath has been already exists, RootPath : {}",rootPathString);
    }

    @Override
    public Mono<Bench> save(Bench bench, Flux<FilePart> filePartFlux) {
        return Mono.defer(()->{
                try {
                    Files.createFile(rootPath);
                } catch (IOException e) {
                    throw new FileServiceException(e);
                }
                return Mono.just(bench);
            }).subscribeOn(Schedulers.boundedElastic())
                .thenMany(filePartFlux)
                .map(filePart -> {
                    return
                }).map(dataBuffer->{
                    dataBuffer.asByteBuffer()
                });
    }

    @Override
    public void delete(Bench bench) {

    }


    private static final class FileNameBytePair{

        private final String name;
        private final Flux<ByteBuf>

    }

}
