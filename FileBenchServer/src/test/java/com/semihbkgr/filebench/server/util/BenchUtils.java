package com.semihbkgr.filebench.server.util;

import com.semihbkgr.filebench.server.model.dto.BenchCreateDto;
import com.semihbkgr.filebench.server.model.Bench;

import java.util.Collections;

public class BenchUtils {

    private static final String DEFAULT_ID = "0000000";
    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_DESCRIPTION = "description";
    private static final long DEFAULT_EXPIRATION_TIME = 10_000_000L;
    private static final int DEFAULT_VIEW_COUNT = 10;

    private static Bench DEFAULT_BENCH;
    private static BenchCreateDto DEFAULT_BENCH_CREATE_DTO;

    public static Bench defaultBench() {
        if (DEFAULT_BENCH == null)
            DEFAULT_BENCH = newDefaultBench();
        return DEFAULT_BENCH;
    }

    public static BenchCreateDto defaultBenchCreateDto() {
        if (DEFAULT_BENCH_CREATE_DTO == null)
            DEFAULT_BENCH_CREATE_DTO = newDefaultBenchCreateDto();
        return DEFAULT_BENCH_CREATE_DTO;
    }

    public static Bench newDefaultBench() {
        return Bench.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .creationTimeMs(System.currentTimeMillis())
                .expirationTimeMs(DEFAULT_EXPIRATION_TIME)
                .files(Collections.emptyList())
                .build();
    }

    public static BenchCreateDto newDefaultBenchCreateDto() {
        return BenchCreateDto.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .expirationDurationMs(DEFAULT_EXPIRATION_TIME)
                .build();
    }

}