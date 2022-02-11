package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("bench")
@JsonView(Bench.Views.BenchDetails.class)
public class Bench {

    @Id
    private String id;

    @JsonView(Bench.Views.BenchSecrets.class)
    private String token;

    private String name;

    private List<File> files;

    private long expirationDurationMs;

    private long creationTimeMs;

    private long viewCount;

    public static class Views {

        public interface BenchDetails {

        }

        public interface BenchSecrets extends BenchDetails {

        }

    }

}
