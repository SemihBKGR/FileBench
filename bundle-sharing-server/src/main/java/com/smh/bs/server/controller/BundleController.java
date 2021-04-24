package com.smh.bs.server.controller;

import com.smh.bs.server.component.BundleIdGenerator;
import com.smh.bs.server.dto.BundleUploadDto;
import com.smh.bs.server.model.Bundle;
import com.smh.bs.server.service.BundleService;
import com.smh.bs.server.service.BundleStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bundle")
@RequiredArgsConstructor
public class BundleController {

    @Value("${bundle.expiration-time.min}")
    private int minExpirationTime;

    @Value("${bundle.expiration-time.max}")
    private int maxExpirationTime;

    @Value("${bundle.expiration-time.def}")
    private int defExpirationTime;

    private final BundleService bundleService;
    private final BundleStorageService bundleStorageService;
    private final BundleIdGenerator bundleIdGenerator;

    @PostMapping(value="/upload")
    public Mono<Bundle> bundleUpload(@RequestBody BundleUploadDto bundleUploadDto){
        Bundle bundle=Bundle.builder()
                .createdTime(System.currentTimeMillis())
                .expirationTime(validateExpirationTime(bundleUploadDto.getExpirationTime()))
                .id(bundleIdGenerator.generate())
                .count(bundleUploadDto.getResources().length)
                .build();
        return Mono.just(bundle)
                .flatMap(b-> bundleStorageService.storeBundle(b,bundleUploadDto))
                .flatMap(bundleService::save);
    }

    private int validateExpirationTime(int expirationTime){
        if(expirationTime>maxExpirationTime) return maxExpirationTime;
        else if(expirationTime<minExpirationTime) return minExpirationTime;
        else return expirationTime;
    }


}
