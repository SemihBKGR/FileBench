package com.semihbkgr.filebench.server.actuator;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.repository.FileRepository;
import com.semihbkgr.filebench.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@WebEndpoint(id = "bench")
public class BenchEndpoint {

    private final BenchRepository benchRepository;
    private final FileRepository fileRepository;
    private final StorageService storageService;

    @Value("${bench.actuator.cache-duration:10000}")
    private volatile long cacheDuration;

    private volatile BenchActuatorInfo lastInfo;
    private volatile long lastCalculationTime = -1;

    @ReadOperation
    public Mono<BenchActuatorInfo> info() {
        if (System.currentTimeMillis() - lastCalculationTime <= cacheDuration)
            return Mono.just(lastInfo);
        return Mono.zip(benchRepository.count(), fileRepository.count(), storageService.size())
                .map(tuple -> new BenchActuatorInfo(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .doOnNext(info -> {
                    this.lastInfo = info;
                    this.lastCalculationTime = System.currentTimeMillis();
                });
    }

    @ReadOperation
    public Mono<String> customEndPointByName(@Selector String selector) {
        if (selector.equals("cache")) {
            return Mono.just(String.valueOf(cacheDuration));
        } else {
            return Mono.just("Unknown selector");
        }
    }

    @WriteOperation
    public Mono<Void> setCacheDuration(@Selector long cacheDuration) {
        this.cacheDuration = cacheDuration;
        return Mono.empty();
    }

    @DeleteOperation
    public Mono<Void> clearCache() {
        lastCalculationTime = -1;
        return Mono.empty();
    }

}
