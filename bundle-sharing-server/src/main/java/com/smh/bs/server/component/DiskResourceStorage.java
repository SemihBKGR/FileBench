package com.smh.bs.server.component;

import com.smh.bs.server.dto.Resource;
import com.smh.bs.server.exception.ResourceStorageException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class DiskResourceStorage implements ResourceStorage {

    private final Path rootPath;

    public DiskResourceStorage(@Value("${bundle.resource.root-directory}") String rootDirectory) {
        rootPath = Path.of(rootDirectory);
    }

    @Override
    @SneakyThrows
    public int store(String subDirectory,Resource resource) {
        Path subPath=rootPath.resolve(Path.of(subDirectory));
        if (!Files.exists(subPath)) Files.createDirectory(subPath);
        Path resourcePath=subPath.resolve(resource.getName());
        Files.write(resourcePath,resource.getData(),StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        return resource.getData().length;
    }

}
