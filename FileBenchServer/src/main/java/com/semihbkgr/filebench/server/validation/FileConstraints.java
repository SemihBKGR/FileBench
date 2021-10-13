package com.semihbkgr.filebench.server.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileConstraints {

    //Name
    private String nameRegex;
    private int nameMinLength;
    private int nameMaxLength;
    private boolean nameRequired;

    //Description
    private String descriptionRegex;
    private int descriptionMinLength;
    private int descriptionMaxLength;
    private boolean descriptionRequired;

    //Label
    private String labelRegex;
    private int labelMinLength;
    private int labelMaxLength;
    private boolean labelRequired;

}
