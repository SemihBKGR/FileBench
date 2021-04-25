package com.smh.bs.server.util;

import com.smh.bs.server.dto.ResourceUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ResourceUtils {


    public static ResourceUpload generateResource(String resourceName) throws IOException {
        byte[] data=Files.readAllBytes(Path.of(System.getProperty("user.dir")).resolve("test-images").resolve(resourceName));
        return ResourceUpload.builder().data(Base64.getUrlEncoder().encode(data)).name(resourceName).build();
    }

}
