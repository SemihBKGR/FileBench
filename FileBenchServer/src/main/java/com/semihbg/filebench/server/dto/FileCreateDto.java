package com.semihbg.filebench.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileCreateDto {

    private String name;

    private List<String> folders;

    private String extension;

    private String label;

    private String description;

}
