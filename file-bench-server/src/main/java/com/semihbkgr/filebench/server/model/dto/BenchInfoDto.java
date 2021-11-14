package com.semihbkgr.filebench.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchInfoDto {

    private String id;
    private long expirationDurationMs;
    private long creationTimeMs;


}
