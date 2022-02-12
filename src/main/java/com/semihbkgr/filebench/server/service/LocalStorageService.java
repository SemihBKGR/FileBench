package com.semihbkgr.filebench.server.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Service
@Slf4j
public class LocalStorageService implements StorageService {

    private final Path rootDirPath;

    public LocalStorageService(@Value("${storage.local.root-dir:files}") String rootDirPath) {
        this.rootDirPath = Path.of(rootDirPath);
    }

    @PostConstruct
    public void createOrClearRootDir() throws IOException {
        if (!Files.exists(rootDirPath)) {
            log.info("RootDir does not exists");
            Files.createDirectory(rootDirPath);
            log.info("RootDir has been created successfully");
        } else {
            log.info("RootDir is already available");
            Files.walkFileTree(rootDirPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!dir.equals(rootDirPath)) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.TERMINATE;
                }
            });
            log.info("RootDir is cleared");
        }
        log.info("RootDir path: {}", rootDirPath);
    }

    @Override
    public Mono<Void> saveFile(@NonNull String dirname, @NonNull String filename, @NonNull FilePart filePart) {
        return Mono.<Path>create(sink -> {
            try {
                var path = Files.createDirectory(resolveBenchPath(dirname));
                sink.success(path);
            } catch (IOException e) {
                sink.error(e);
            }
        }).flatMap(path -> filePart.transferTo(resolveFilePath(path, filename)));
    }

    @Override
    public Flux<DataBuffer> getFile(@NonNull String dirname, @NonNull String filename) {
        return DataBufferUtils.read(resolveFilePath(dirname, filename), new DefaultDataBufferFactory(), StreamUtils.BUFFER_SIZE);
    }

    @Override
    public Mono<Void> deleteFile(@NonNull String dirname, @NonNull String filename) {
        return Mono.create(voidMonoSink -> {
            var path = resolveFilePath(dirname, filename);
            if (Files.exists(path))
                try {
                    Files.delete(path);
                    voidMonoSink.success();
                } catch (IOException e) {
                    voidMonoSink.error(e);
                }
            else
                voidMonoSink.error(new IllegalArgumentException("No such file, dirname: " + dirname + ", filename: " + filename));
        });
    }

    @Override
    public Mono<Void> deleteBench(@NonNull String dirname) {
        return Mono.create(voidMonoSink -> {
            try {
                if (FileSystemUtils.deleteRecursively(resolveBenchPath(dirname)))
                    voidMonoSink.success();
                else
                    voidMonoSink.error(new IllegalArgumentException("No such bench, dirname: " + dirname));
            } catch (IOException e) {
                voidMonoSink.error(e);
            }
        });
    }

    private Path resolveBenchPath(@NonNull String dirname) {
        return rootDirPath.resolve(dirname);
    }

    private Path resolveFilePath(@NonNull Path dirPath, @NonNull String filename) {
        return dirPath.resolve(filename);
    }

    private Path resolveFilePath(@NonNull String dirname, @NonNull String filename) {
        return rootDirPath.resolve(dirname).resolve(filename);
    }

}
