package com.semihbkgr.filebench.server.actuator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BenchActuatorInfo {

    @JsonProperty("bench_count")
    private long benchCount;

    @JsonProperty("file_count")
    private long fileCount;

    private long size;

}
