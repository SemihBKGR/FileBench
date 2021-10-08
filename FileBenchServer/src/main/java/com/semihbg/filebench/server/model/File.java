package com.semihbg.filebench.server.model;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    @Id
    @NonNull
    private String id;
    private String name;
    private String path;
    private String label;
    private String description;
    private int size;
    private long downloadCount;

}
