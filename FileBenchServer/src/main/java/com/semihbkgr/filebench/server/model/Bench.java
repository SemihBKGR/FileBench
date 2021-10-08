package com.semihbkgr.filebench.server.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bench")
@With
public class Bench {

    @Id
    private String id;
    private String name;
    private String description;
    private List<File> files;
    private long creationTimeMs;
    private long expirationTimeMs;
    private long viewCount;

}
