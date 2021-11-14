package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(Bench.Views.BenchDetails.class)
public class File {

    private String id;

    private String name;

    private String description;

    private String label;

    private long size;

    private long downloadCount;

}
