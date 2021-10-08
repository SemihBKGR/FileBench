package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.model.Bench;
import com.semihbkgr.filebench.server.validation.constraint.BenchConstraints;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class BenchValidator implements Validator<Bench> {

    private final BenchConstraints benchConstraints;

    private final Pattern namePattern;
    private final Pattern descriptionPattern;

    public BenchValidator(BenchConstraints benchConstraints) {
        log.info("Bench validation is started with '{}' BenchConstraints", benchConstraints);
        this.benchConstraints = benchConstraints;
        this.namePattern = Pattern.compile(benchConstraints.getNameRegex());
        this.descriptionPattern = Pattern.compile(benchConstraints.getDescriptionRegex());
    }

    @Override
    public ValidationResult validate(Bench bench) {

        //set name as null if blank otherwise strip
        bench.setName(bench.getName().isBlank()?null:bench.getName().strip());
        //set description as null if blank otherwise strip
        bench.setDescription(bench.getDescription().isBlank()?null:bench.getDescription().strip());

        ValidationResult validationResult = ValidationResult.empty();

        //Check Name constraints
        if (bench.getName() == null) {//Check if null
            if(benchConstraints.isNameRequired())
                validationResult.addInvalidation(ValidationResult.InvalidMessage
                        .builder()
                        .type(String.class)
                        .field("name")
                        .message("name cannot be null")
                        .build());
        }else if(bench.getName().length()< benchConstraints.getNameMinLength() ||
                bench.getName().length()> benchConstraints.getNameMaxLength()) {
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message(String.format("name length must be between %d and %d",
                            benchConstraints.getNameMinLength(), benchConstraints.getNameMaxLength()))
                    .build());
        } else if (!namePattern.matcher(bench.getName()).matches()) {//Check if not matches regex pattern
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message("name contains illegal characters")
                    .build());
        }

        //Check Description constraints
        if(bench.getDescription()==null){
            if(benchConstraints.isDescriptionRequired())
                validationResult.addInvalidation(ValidationResult.InvalidMessage
                        .builder()
                        .type(String.class)
                        .field("description")
                        .message("description cannot be null")
                        .build());
        }else if(bench.getDescription().length()< benchConstraints.getDescriptionMinLength() ||
                bench.getDescription().length()> benchConstraints.getDescriptionMaxLength()) {
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message(String.format("description length must be between %d and %d",
                            benchConstraints.getDescriptionMinLength(), benchConstraints.getDescriptionMaxLength()))
                    .build());
        } else if (!descriptionPattern.matcher(bench.getDescription()).matches()) {//Check if not matches regex pattern
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message("description contains illegal characters")
                    .build());
        }

        //Check expiration
//        if(bench.getExpireTime()< benchConstraints.getExpirationMinDuration().toMillis()){
//            validationResult.addInvalidation(ValidationResult.InvalidMessage
//                    .builder()
//                    .type(Long.class)
//                    .field("expiration")
//                    .message(String.format("expiration must be bigger than %d ms",
//                            benchConstraints.getExpirationMinDuration().toMillis()))
//                    .build());
//        }else if(bench.getExpireTime()> benchConstraints.getExpirationMaxDuration().toMillis()){
//            validationResult.addInvalidation(ValidationResult.InvalidMessage
//                    .builder()
//                    .type(Long.class)
//                    .field("expiration")
//                    .message(String.format("expiration must be smaller than %d ms",
//                            benchConstraints.getExpirationMaxDuration().toMillis()))
//                    .build());
//        }

        return validationResult;
    }

    public void validateAndThrowIfInvalid(@NonNull Bench bench){
        validate(bench).throwIfInvalid();
    }

    public void validateAndThrowIfInvalid(@NonNull Bench bench,String message){
        validate(bench).throwIfInvalid(message);
    }

}
