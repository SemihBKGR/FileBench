package com.semihbkgr.filebench.server.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchInfo {

    private int id;
    private long expirationDuration;
    private long creationTime;

}