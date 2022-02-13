package com.semihbkgr.filebench.server.component;

import com.semihbkgr.filebench.server.repository.BenchRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class BenchMetricManager {

    private final String memorySizeName;
    private final AtomicLong size;

    public BenchMetricManager(@Value("${bench.metric.memory-size-name}") String memorySizeName,
                              MeterRegistry meterRegistry, BenchRepository benchRepository) {
        this.memorySizeName = memorySizeName;
        this.size = new AtomicLong();
        benchRepository.allFilesSize().subscribe(s -> {
            size.set(s);
            meterRegistry.gauge(memorySizeName, size);
        });
    }

    public void changeConsumedMemory(long s) {
        size.addAndGet(s);
    }

    public String getMemorySizeName() {
        return memorySizeName;
    }

    public long getSize() {
        return size.get();
    }

}
