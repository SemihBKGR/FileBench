package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("files")
@JsonView(Bench.Views.BenchDetails.class)
public class File {

    private Integer id;

    @Column("bench_id")
    private Integer benchId;

    private String name;

    private long size;

    private long downloadCount;

}