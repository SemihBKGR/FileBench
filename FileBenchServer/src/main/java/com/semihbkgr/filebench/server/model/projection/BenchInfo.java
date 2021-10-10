package com.semihbkgr.filebench.server.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchInfo {

    private String id;
    private long expirationTimeMs;

}
