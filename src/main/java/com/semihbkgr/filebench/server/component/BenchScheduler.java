package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class BenchScheduler implements ApplicationRunner {

    private final BenchRepository benchRepository;
    private final StorageService storageService;

    @Value("${bench.expiration.check-duration:30s}")
    private Duration checkDuration;

    @Override
    public void run(ApplicationArguments args) {
        Flux.interval(checkDuration)
                .thenMany(benchRepository.findAllExpiredBenchInfos())
                .flatMap(info ->
                        storageService.deleteBench(info.getDirname())
                                .onErrorResume(e -> Mono.empty())
                                .then(benchRepository.deleteById(info.getId()))
                                .onErrorResume(e -> Mono.empty()))
                .count()
                .subscribe(count -> {
                    if (count > 0)
                        log.info("ExpiredBenchCount: {}", count);
                });
    }

}
