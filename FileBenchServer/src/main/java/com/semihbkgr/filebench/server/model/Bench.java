package com.semihbkgr.filebench.server.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bench")
@With
@JsonView(Bench.Views.BenchInfo.class)
public class Bench {

    @Id
    @JsonView(Views.BenchReadAccess.class)
    private String id;

    @Indexed(unique = true)
    @JsonView(Views.BenchWriteAccess.class)
    private String updateToken;

    private String name;
    private String description;

    @JsonView(Views.BenchReadAccess.class)
    private List<File> files;
    private long creationTimeMs;
    private long expirationTimeMs;
    private long viewCount;

    public interface Views {

        interface BenchInfo {
        }

        interface BenchReadAccess extends BenchInfo {
        }

        interface BenchWriteAccess extends BenchReadAccess {
        }

    }

}
