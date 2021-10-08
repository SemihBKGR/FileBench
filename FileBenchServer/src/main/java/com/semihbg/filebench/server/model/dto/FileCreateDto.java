package com.semihbg.filebench.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileCreateDto {

    private String name;
    private String label;
    private String description;

}
