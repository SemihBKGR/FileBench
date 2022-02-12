package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("files")
@JsonView(Bench.Views.BenchDetails.class)
public class File {

    @Id
    @With
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column("bench_id")
    @JsonProperty(value = "bench_id", access = JsonProperty.Access.READ_ONLY)
    private Integer benchId;

    @JsonIgnore
    private String filename;

    private String name;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long size;

    @Column("download_count")
    @JsonProperty(value = "download_count", access = JsonProperty.Access.READ_ONLY)
    private long downloadCount;

}