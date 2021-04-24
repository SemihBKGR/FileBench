package com.smh.bs.server.component;

import com.smh.bs.server.dto.Resource;

public interface ResourceStorage {

    int store(String subDirectory, Resource resource);

}
