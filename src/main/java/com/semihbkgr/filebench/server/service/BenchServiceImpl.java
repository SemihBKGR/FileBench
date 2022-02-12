package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.model.projection.BenchInfo;
import com.semihbkgr.filebench.server.repository.BenchRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BenchServiceImpl implements BenchService {

    private final BenchRepository repository;

    @Override
    public Mono<Bench> save(@NonNull Bench bench) {
        return repository.save(bench);
    }

    @Override
    public Mono<Bench> findByAccessToken(@NonNull String token) {
        return repository.findByAccessToken(token);
    }

    @Override
    public Mono<Bench> findByEditToken(@NonNull String token) {
        return repository.findByEditToken(token);
    }

    @Override
    public Flux<BenchInfo> findAllInfos() {
        return repository.findAllBy();
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return repository.deleteById(id);
    }

}
