package com.semihbkgr.filebench.server.actuator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BenchActuatorInfo {

    private long benchCount;

    private long fileCount;

    private long size;

}
