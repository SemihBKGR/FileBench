package com.semihbkgr.filebench.server.actuator;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import com.semihbkgr.filebench.server.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@WebEndpoint(id = "bench")
public class BenchEndpoint {

    private final BenchRepository benchRepository;
    private final FileRepository fileRepository;

    @Value("${bench.actuator.cache-duration:10000ms}")
    private volatile Duration cacheDuration;

    private volatile BenchActuatorInfo lastInfo;
    private volatile long lastCalculationTimeMS = -1;

    @ReadOperation
    public Mono<BenchActuatorInfo> info() {
        if (System.currentTimeMillis() - lastCalculationTimeMS <= cacheDuration.toMillis())
            return Mono.just(lastInfo);
        return Mono.zip(benchRepository.count(), fileRepository.count(), benchRepository.allFilesSize())
                .map(tuple -> new BenchActuatorInfo(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .doOnNext(info -> {
                    this.lastInfo = info;
                    this.lastCalculationTimeMS = System.currentTimeMillis();
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
    public Mono<Void> setCacheDuration(@Selector long cacheDurationMS) {
        this.cacheDuration = Duration.ofMillis(cacheDurationMS);
        return Mono.empty();
    }

    @DeleteOperation
    public Mono<Void> clearCache() {
        lastCalculationTimeMS = -1;
        return Mono.empty();
    }

}
