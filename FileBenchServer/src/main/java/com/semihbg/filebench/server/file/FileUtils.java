package com.semihbg.filebench.server.file;

import java.nio.file.Path;

public class FileUtils {

    public static void createFile(Path path, String fileName){
        path.resolve(fileName);
    }

}
