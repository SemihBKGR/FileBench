package com.smh.bs.server.service;

import com.smh.bs.server.model.Bundle;
import com.smh.bs.server.repository.BundleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BundleServiceImpl implements BundleService {

    private final BundleRepository bundleRepository;

    @Override
    public Mono<Bundle> save(Bundle bundle) {
        return bundleRepository.save(bundle);
    }

    @Override
    public Mono<Bundle> findById(String id) {
        return bundleRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bundleRepository.deleteById(id);
    }

}
