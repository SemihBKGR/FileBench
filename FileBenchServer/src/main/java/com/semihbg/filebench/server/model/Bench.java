package com.semihbg.filebench.server.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bench")
public class Bench {

    @Id
    private String id;

    private String name;

    private String description;

    private List<File> files;

    @CreatedDate
    private long createdTime;

    private long expireTime;

    private long viewCount;

}
