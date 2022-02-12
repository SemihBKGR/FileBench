package com.semihbkgr.filebench.server.actuator;

import com.semihbkgr.filebench.server.config.BenchConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@WebEndpoint(id = "bench-properties")
public class BenchPropertiesEndpoint {

    private final BenchConfig.BenchProperties properties;

    @ReadOperation
    public Mono<BenchConfig.BenchProperties> properties() {
        return Mono.just(properties);
    }

}
