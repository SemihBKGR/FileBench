package com.smh.bs.server.util;

import com.smh.bs.server.dto.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ResourceUtils {


    public static Resource generateResource(String resourceName) throws IOException {
        byte[] data=Files.readAllBytes(Path.of(System.getProperty("user.dir")).resolve("test-images").resolve(resourceName));
        return Resource.builder().data(Base64.getUrlEncoder().encode(data)).name(resourceName).build();
    }

}
