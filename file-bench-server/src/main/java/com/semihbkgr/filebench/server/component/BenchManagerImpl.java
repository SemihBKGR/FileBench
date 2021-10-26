package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.service.BenchService;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@EnableAsync
@Component
@RequiredArgsConstructor
@Slf4j
public class BenchManagerImpl implements BenchManager {

    private final BenchService benchService;
    private final StorageService storageService;

    @Scheduled(fixedDelayString = "${bench.manager.expiration-check-time-ms:0}")
    @Async
    @Override
    public void checkForExpiredBench() {
        long currentTimeMs = System.currentTimeMillis();
        benchService.findAllInfo()
                .filter(benchInfo -> currentTimeMs >= (benchInfo.getCreationTimeMs() + benchInfo.getExpirationDurationMs()))
                .flatMap(benchInfo -> {
                    log.info("Bench has been expired, benchId: {}",benchInfo.getId());
                    return benchService.deleteById(benchInfo.getId())
                            .then(storageService.deleteBench(benchInfo.getId()));
                })
                .blockLast();
    }

}
