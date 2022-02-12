package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("benches")
@JsonView(Bench.Views.BenchDetails.class)
public class Bench {

    @Id
    @With
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column("access_token")
    @JsonProperty(value = "access_token", access = JsonProperty.Access.READ_ONLY)
    private String accessToken;

    @Column("edit_token")
    @JsonProperty(value = "edit_token", access = JsonProperty.Access.READ_ONLY)
    @JsonView(Bench.Views.BenchSecrets.class)
    private String editToken;

    @JsonIgnore
    private String dirname;

    @Column("file_count")
    @JsonProperty(value = "file_count", access = JsonProperty.Access.READ_ONLY)
    private int fileCount;

    @Column("file_size")
    @JsonProperty(value = "file_size", access = JsonProperty.Access.READ_ONLY)
    private long fileSize;

    @Column("expiration_duration")
    @JsonProperty("expiration_duration")
    private long expirationDuration;

    @Column("creation_time")
    @JsonProperty(value = "creation_time", access = JsonProperty.Access.READ_ONLY)
    private long creationTime;

    @Column("view_count")
    @JsonProperty(value = "view_count", access = JsonProperty.Access.READ_ONLY)
    private long viewCount;

    private String name;

    private String description;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<File> files = Collections.emptyList();

    public static class Views {

        public interface BenchDetails {

        }

        public interface BenchSecrets extends BenchDetails {

        }

    }

}