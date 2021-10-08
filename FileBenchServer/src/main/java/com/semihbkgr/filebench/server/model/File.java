package com.semihbkgr.filebench.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String id;
    private String name;
    private String label;
    private String description;
    private long size;
    private long downloadCount;

}
