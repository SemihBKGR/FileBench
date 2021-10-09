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
    private String description;
    private String label;
    private long size;
    private long downloadCount;

}
