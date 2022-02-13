package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class BenchMetricManager {


    private final String memorySizeName;
    private final MeterRegistry meterRegistry;
    private final AtomicLong size;

    public BenchMetricManager(@Value("${bench.metric.memory-size-name}") String memorySizeName, MeterRegistry meterRegistry, BenchRepository benchRepository) {
        this.memorySizeName = memorySizeName;
        this.meterRegistry = meterRegistry;
        this.size = new AtomicLong();
        benchRepository.allFilesSize().subscribe(s -> {
            size.set(s);
            meterRegistry.gauge(memorySizeName, s);
        });
    }

    public void changeConsumedMemory(long s) {
        meterRegistry.gauge(memorySizeName, size.addAndGet(s));
    }

}
