package com.semihbg.filebench.server.model;

import com.mongodb.lang.NonNull;
import lombok.*;
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
    @NonNull
    private String id;
    private String name;
    private String description;
    private List<File> files;
    private long createdTime;
    private long expireTime;
    private long viewCount;

}
