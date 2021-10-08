package com.semihbg.filebench.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenchCreateDto {

    private String name;
    private String description;
    private List<FileCreateDto> files;
    private long expireTime;

}
