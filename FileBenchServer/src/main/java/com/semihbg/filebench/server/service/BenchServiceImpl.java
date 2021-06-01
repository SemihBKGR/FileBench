package com.semihbg.filebench.server.service;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.repository.BenchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BenchServiceImpl implements BenchService {

    private final BenchRepository benchRepository;

    @Override
    public Mono<Bench> save(Bench bench) {
        return benchRepository.save(bench);
    }

    @Override
    public Mono<Bench> findById(String id) {
        return benchRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return benchRepository.deleteById(id);
    }

}
