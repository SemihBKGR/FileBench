package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(Bench.Views.BenchInfo.class)
public class File {

    private String id;

    @NotNull(message = "File name must not be null")
    private String name;
    private String description;
    private String label;
    private long size;
    private long downloadCount;

}
