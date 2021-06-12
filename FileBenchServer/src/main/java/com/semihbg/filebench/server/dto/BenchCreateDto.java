package com.semihbg.filebench.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchCreateDto {

    private String name;

    private String description;

    private List<FileCreateDto> files;

    private long expireTime;

}
