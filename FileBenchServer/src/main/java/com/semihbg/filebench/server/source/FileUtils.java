package com.semihbg.filebench.server.source;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    //Simple Sneaky throw utility
    @SneakyThrows(IOException.class)
    public static void createDirectories(Path path){
        Files.createDirectories(path);
    }

    //Simple Sneaky throw utility
    @SneakyThrows(IOException.class)
    public static void createFile(Path path){
        Files.createFile(path);
    }

}
