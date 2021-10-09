package com.semihbkgr.filebench.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenchCreateDto {

    private String name;
    private String description;
    private long expirationDurationMs;

}