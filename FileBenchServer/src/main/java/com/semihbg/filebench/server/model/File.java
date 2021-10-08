package com.semihbg.filebench.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String name;
    private String path;
    private String label;
    private String description;

    private int size;
    private long downloadCount;

}
