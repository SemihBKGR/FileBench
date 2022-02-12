package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("benches")
@JsonView(Bench.Views.BenchDetails.class)
public class Bench {

    @Id
    private Integer id;

    @Column("access_token")
    private String accessToken;

    @Column("edit_token")
    @JsonView(Bench.Views.BenchSecrets.class)
    private String editToken;

    @JsonIgnore
    private String dirname;

    private String name;

    private String description;

    @Transient
    private List<File> files;

    @Column("expiration_duration")
    private long expirationDuration;

    @Column("creation_time")
    private long creationTime;

    @Column("view_count")
    private long viewCount;

    public static class Views {

        public interface BenchDetails {

        }

        public interface BenchSecrets extends BenchDetails {

        }

    }

}