package com.semihbg.filebench.server.service;

import ch.qos.logback.core.rolling.helper.FileNamePattern;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalStorageService implements StorageService {

    private final Path rootDirPath;

    public LocalStorageService(@Value("${storage.local.root-dir:files}") String rootDirPath) {
        this.rootDirPath = Path.of(rootDirPath);
    }

    @Override
    public Mono<Void> saveFile(@NonNull String benchId, @NonNull String fileId, @NonNull Mono<FilePart> filePartMono) {
        return filePartMono
                .doOnNext(filePart -> filePart.transferTo(resolveFilePath(benchId,fileId)))
                .then();
    }

    @Override
    public Flux<DataBuffer> getFile(@NonNull String benchId, @NonNull String fileId) {
        return DataBufferUtils.read(resolveFilePath(benchId, fileId), new DefaultDataBufferFactory(), StreamUtils.BUFFER_SIZE);
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
