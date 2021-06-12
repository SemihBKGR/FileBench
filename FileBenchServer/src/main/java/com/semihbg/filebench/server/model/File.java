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

    private List<String> folders;

    private String extension;

    private String label;

    private String description;

    private int size;

    private long downloadCount;

    public static File of(@NonNull FileCreateDto fileCreateDto){
        return File.builder()
                .name(fileCreateDto.getName())
                .folders(fileCreateDto.getFolders())
                .extension(fileCreateDto.getExtension())
                .label(fileCreateDto.getLabel())
                .description(fileCreateDto.getDescription())
                .build();
    }

}
