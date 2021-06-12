package com.semihbg.filebench.server.model;

import com.semihbg.filebench.server.dto.BenchCreateDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
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

    private long createdTime;

    private long expireTime;

    private long viewCount;

    public static Bench of(@NonNull BenchCreateDto benchCreateDto) {
        return Bench.builder()
                .name(benchCreateDto.getName())
                .description(benchCreateDto.getDescription())
                .expireTime(benchCreateDto.getExpireTime())
                .build();
    }

}
