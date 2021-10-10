package com.semihbkgr.filebench.server.component;

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
public class BenchManagerImpl implements BenchManager {

    @Scheduled(fixedDelayString = "${bench.manager.expiration-check-time-ms:0")
    @Async
    @Override
    public void checkForExpiredBench() {

    }

}
