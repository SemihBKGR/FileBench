package com.semihbg.filebench.server.file;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    @SneakyThrows(IOException.class)
    public static void createDirectories(Path path){
        Files.createDirectories(path);
    }

    @SneakyThrows(IOException.class)
    public static void createFile(Path path){
        Files.createFile(path);
    }

}
