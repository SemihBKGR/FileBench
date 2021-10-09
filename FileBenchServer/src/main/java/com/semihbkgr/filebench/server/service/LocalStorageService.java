package com.semihbkgr.filebench.server.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class LocalStorageService implements StorageService {

    private final Path rootDirPath;

    public LocalStorageService(@Value("${storage.local.root-dir:files}") String rootDirPath) {
        this.rootDirPath = Path.of(rootDirPath);
    }

    @PostConstruct
    public void createRootDirIfNotExists() throws IOException {
        if (!Files.exists(rootDirPath)) {
            log.info("RootDir does not exists");
            Files.createDirectory(rootDirPath);
            log.info("RootDir has been created successfully");
        } else
            log.info("RootDir is already available");
        log.info("RootDir path: {}", rootDirPath);
    }

    @Override
    public Mono<Void> saveFile(@NonNull String benchId, @NonNull String fileId, @NonNull Mono<FilePart> filePartMono) {
        return Mono.<Path>create(voidMonoSink -> {
            try {
                var directoryPath = resolveBenchPath(benchId);
                if (!Files.exists(directoryPath))
                    Files.createDirectory(directoryPath);
                var filePath = resolveFilePath(benchId, fileId);
                if (!Files.exists(filePath))
                    Files.createFile(filePath);
                voidMonoSink.success(filePath);
            } catch (IOException e) {
                voidMonoSink.error(e);
            }
        }).flatMap(filePath -> filePartMono.flatMap(filePart -> filePart.transferTo(filePath)));
    }

    @Override
    public Mono<Void> updateFile(String benchId, String fileId, Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> filePart.transferTo(resolveFilePath(benchId,fileId)));
    }

    @Override
    public Flux<DataBuffer> getFile(@NonNull String benchId, @NonNull String fileId) {
        return DataBufferUtils.read(resolveFilePath(benchId, fileId), new DefaultDataBufferFactory(), 256);
    }

    @Override
    public Mono<Void> deleteBench(@NonNull String benchId) {
        return Mono.create(voidMonoSink -> {
            try {
                Files.delete(resolveBenchPath(benchId));
            } catch (IOException e) {
                voidMonoSink.error(e);
            }
            voidMonoSink.success();
        });
    }

    @Override
    public Mono<Void> deleteFile(@NonNull String benchId, @NonNull String fileId) {
        return Mono.create(voidMonoSink -> {
            try {
                Files.delete(resolveFilePath(benchId, fileId));
            } catch (IOException e) {
                voidMonoSink.error(e);
            }
            voidMonoSink.success();
        });
    }

    private Path resolveBenchPath(@NonNull String benchId) {
        return rootDirPath.resolve(benchId);
    }

    private Path resolveFilePath(@NonNull String benchId, @NonNull String fileId) {
        return rootDirPath.resolve(benchId).resolve(fileId);
    }

}
