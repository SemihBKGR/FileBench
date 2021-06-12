package com.semihbg.filebench.server.file;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class LocalFileSource implements FileSource {

    private final String rootDirectoryPathString;
    private final Path rootDirectoryPath;
    private final AtomicBoolean isRootDirectoryExists;

    public LocalFileSource(@Value("${source.root-path:source}") String rootDirectoryPathString) {
        this.rootDirectoryPathString = rootDirectoryPathString;
        this.rootDirectoryPath = Path.of(rootDirectoryPathString);
        this.isRootDirectoryExists = new AtomicBoolean(Files.exists(rootDirectoryPath));
    }

    @SneakyThrows(IOException.class)
    public void createRootFolderIfNotExists() {
        if (!Files.exists(rootDirectoryPath)) {
            Files.createDirectory(rootDirectoryPath);
            isRootDirectoryExists.set(true);
            log.info("File has been created, path : {}", rootDirectoryPathString);
        } else log.info("File is already exists, path : {}", rootDirectoryPathString);
    }

    @SneakyThrows(IOException.class)
    public void deleteRootFolderIfNotExists() {
        if (Files.exists(rootDirectoryPath)) {
            Files.delete(rootDirectoryPath);
            isRootDirectoryExists.set(false);
            log.info("File has been deleted, path : {}", rootDirectoryPathString);
        } else log.info("File is not already exists, path : {}", rootDirectoryPathString);
    }

    public boolean isRootDirectoryExists(){
        return isRootDirectoryExists.get();
    }

    private void checkIfRootDirectoryExists() {
        if (!isRootDirectoryExists.get())
            throw new IllegalStateException("File is not exists");
    }

    @Override
    public void save(FileContext fileContext) throws IOException {
        checkIfRootDirectoryExists();
        Path currentPath = rootDirectoryPath.resolve(fileContext.getBench().getId());
        Files.createDirectory(currentPath);
        fileContext.getFilePartFlux()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(filePart -> {
                    File file = fileContext.findFileByName(filePart.filename());
                    StringJoiner directoryStringJoiner = new StringJoiner("/", "/", "");
                    file.getFolders().forEach(directoryStringJoiner::add);
                    Path directoriesPath = currentPath.resolve(directoryStringJoiner.toString());
                    FileUtils.createDirectories(directoriesPath);
                    Path thisFilePath = directoriesPath.resolve(file.getName());
                    FileUtils.createFile(thisFilePath);
                    filePart.transferTo(thisFilePath);
                });
    }

    @Override
    public void delete(Bench bench) {
        checkIfRootDirectoryExists();

    }

}
