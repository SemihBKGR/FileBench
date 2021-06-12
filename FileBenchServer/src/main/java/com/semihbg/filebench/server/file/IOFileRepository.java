package com.semihbg.filebench.server.file;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class IOFileRepository implements FileRepository {

    private final String filePathString;
    private final Path filePath;
    private final AtomicBoolean isFileExists;

    public IOFileRepository(@Value("${file.path:files}") String  filePathString) {
        this. filePathString =  filePathString;
        this.filePath=Path.of(filePathString);
        this.isFileExists=new AtomicBoolean(false);
        createFileIfNotExists();
    }

    @SneakyThrows(IOException.class)
    private void createFileIfNotExists(){
        if(!Files.exists(filePath)){
            Files.createFile(filePath);
            isFileExists.set(true);
            log.info("File has been created, path : {}",filePathString);
        }else log.info("File is already exists, path : {}",filePathString);
    }

    @SneakyThrows(IOException.class)
    private void deleteFileIfExists(){
        if(Files.exists(filePath)){
            Files.delete(filePath);
            isFileExists.set(false);
            log.info("File has been deleted, path : {}",filePathString);
        }else log.info("File is not already exists, path : {}",filePathString);
    }

    private void checkIfFileExists(){
        if(!isFileExists.get())
            throw new IllegalStateException("File is not exists");
    }

    @Override
    public void save(FileContext fileContext) throws IOException {
        checkIfFileExists();
        Path currentPath=filePath.resolve(fileContext.getBench().getId());
        Files.createDirectory(currentPath);
        fileContext.getFilePartFlux().subscribe(filePart -> {
            File file=fileContext.findFileByName(filePart.filename());
            StringJoiner directoryStringJoiner=new StringJoiner("/","/","");
            file.getFolders().forEach(directoryStringJoiner::add);
            Path thisFilePath=currentPath.resolve(directoryStringJoiner.toString());
            try {
                Files.createDirectories(thisFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        })
    }

    @Override
    public void delete(Bench bench) {
        checkIfFileExists();

    }

}
