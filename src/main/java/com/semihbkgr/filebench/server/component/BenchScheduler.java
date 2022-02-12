package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class BenchScheduler {

    private final BenchRepository benchRepository;
    private final StorageService storageService;

    @Scheduled(fixedRateString = "${bench.expiration.check-duration}")
    public void deleteExpiredBenches() {
        benchRepository.findAllExpiredBenchInfos()
                .flatMap(info ->
                        storageService.deleteBench(info.getDirname())
                                .then(benchRepository.deleteById(info.getId())))
                .count()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(count -> {
                    if (count > 0)
                        log.info("ExpiredBenchCount: {}", count);
                });
    }

}
