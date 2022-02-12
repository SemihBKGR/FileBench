package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class BenchScheduler {

    private final BenchRepository benchRepository;
    private final StorageService storageService;

    //@Scheduled(fixedRateString = "${bench.expiration.check-duration}")
    public void deleteExpiredBenches() {
        benchRepository.findAllExpiredBenchInfos()
                .flatMap(info ->
                        storageService.deleteBench(info.getDirname())
                                .onErrorResume(e -> Mono.empty())
                                .then(benchRepository.deleteById(info.getId()))
                                .onErrorResume(e -> Mono.empty()))
                .count()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(count -> {
                    if (count > 0)
                        log.info("ExpiredBenchCount: {}", count);
                });
    }

}
