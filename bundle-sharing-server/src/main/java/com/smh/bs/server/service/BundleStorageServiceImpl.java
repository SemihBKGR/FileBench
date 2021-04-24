package com.smh.bs.server.service;

import com.smh.bs.server.component.ResourceStorage;
import com.smh.bs.server.dto.BundleUploadDto;
import com.smh.bs.server.dto.Resource;
import com.smh.bs.server.model.Bundle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BundleStorageServiceImpl implements BundleStorageService {

    private final ResourceStorage resourceStorage;

    @Override
    public Mono<Bundle> storeBundle(Bundle bundle, BundleUploadDto bundleUploadDto) {
        return Mono.defer(() -> {
            int bytes=0;
            for (Resource resource : bundleUploadDto.getResources()) {
                //TODO validate extensions
                bytes+=resourceStorage.store(bundle.getId(),resource);
            }
            bundle.setBytes(bytes);
            return Mono.just(bundle);
        });
    }

    @Override
    public Mono<Void> deleteBundle(Bundle bundle) {
        return null;
    }

}