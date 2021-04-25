package com.smh.bs.server.service;

import com.smh.bs.server.component.ResourceStorage;
import com.smh.bs.server.dto.BundleUploadDto;
import com.smh.bs.server.dto.ResourceUpload;
import com.smh.bs.server.model.Bundle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BundleStorageServiceImpl implements BundleStorageService {

    private final ResourceStorage resourceStorage;

    @Override
    public Mono<Bundle> storeBundle(Bundle bundle, BundleUploadDto bundleUploadDto) {
        return Mono.defer(() -> {
            int bytes=0;
            for (ResourceUpload resource : bundleUploadDto.getResources()) {
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