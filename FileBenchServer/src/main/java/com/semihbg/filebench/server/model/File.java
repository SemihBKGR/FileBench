package com.semihbg.filebench.server.model;

import com.semihbg.filebench.server.dto.FileCreateDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String name;

    private String path;

    private String label;

    private String description;

    private int size;

    private long downloadCount;

    public static File of(@NonNull FileCreateDto fileCreateDto){
        return File.builder()
                .name(fileCreateDto.getName())
                .path(fileCreateDto.getPath())
                .label(fileCreateDto.getLabel())
                .description(fileCreateDto.getDescription())
                .size(fileCreateDto.getSize())
                .build();
    }

}
