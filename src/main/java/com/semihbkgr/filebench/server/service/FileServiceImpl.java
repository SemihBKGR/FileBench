package com.semihbkgr.filebench.server.service;

import com.semihbkgr.filebench.server.model.File;
import com.semihbkgr.filebench.server.repository.FileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository repository;

    @Override
    public Mono<File> save(@NonNull File file) {
        return repository.save(file);
    }

    @Override
    public Flux<File> findAllByBenchId(int id) {
        return repository.findAllByBenchId(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return repository.deleteById(id);
    }

}
