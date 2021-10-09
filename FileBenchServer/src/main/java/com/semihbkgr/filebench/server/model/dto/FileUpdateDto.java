package com.semihbkgr.filebench.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUpdateDto {

    private String name;
    private String label;
    private String description;

}
