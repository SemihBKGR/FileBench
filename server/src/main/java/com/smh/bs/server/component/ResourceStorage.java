package com.smh.bs.server.component;

import com.smh.bs.server.dto.ResourceUpload;

public interface ResourceStorage {

    int store(String subDirectory, ResourceUpload resource);

}
