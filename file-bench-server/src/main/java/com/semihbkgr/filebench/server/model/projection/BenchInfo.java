package com.semihbkgr.filebench.server.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchInfo {

    private String id;
    private long expirationDurationMs;
    private long creationTimeMs;

}
