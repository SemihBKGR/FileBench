package com.semihbkgr.filebench.server.validation;

import lombok.*;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchConstraints {

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

    //Expiration
    private Duration expirationMinDuration;
    private Duration expirationMaxDuration;

}
