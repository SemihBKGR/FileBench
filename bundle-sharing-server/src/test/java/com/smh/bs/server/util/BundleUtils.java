package com.smh.bs.server.util;

import com.smh.bs.server.dto.BundleUploadDto;
import com.smh.bs.server.dto.Resource;
import com.smh.bs.server.model.Bundle;

import java.io.IOException;

public class BundleUtils {

    private static final String[] testImages;

    static{
        testImages=new String[]{"test01.jpg","test02.jpg"};
    }

    public static BundleUploadDto generateBundleUploadDto() throws IOException {
        Resource[] resources=new Resource[testImages.length];
        for(int i=0;i<resources.length;i++)
            resources[i++]= ResourceUtils.generateResource(testImages[i]);
        return BundleUploadDto.builder()
                .resources(resources)
                .expirationTime(20000)
                .build();
    }

}
