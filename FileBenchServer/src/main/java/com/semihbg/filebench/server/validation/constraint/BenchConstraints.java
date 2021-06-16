package com.semihbg.filebench.server.validation.constraint;

import lombok.*;

import java.time.Duration;
import java.util.concurrent.Executors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
