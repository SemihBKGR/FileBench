package com.semihbg.filebench.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String name;

    private List<String> folders;

    private String extension;

    private String label;

    private String description;

    private int size;

    private long downloadCount;

}
