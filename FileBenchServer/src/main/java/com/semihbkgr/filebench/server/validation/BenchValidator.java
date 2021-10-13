package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.model.Bench;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class BenchValidator implements Validator<Bench> {

    private final BenchConstraint benchConstraint;

    private final Pattern namePattern;
    private final Pattern descriptionPattern;

    public BenchValidator(BenchConstraint benchConstraint) {
        log.info("Bench validation is started with '{}' BenchConstraint", benchConstraint);
        this.benchConstraint = benchConstraint;
        this.namePattern = Pattern.compile(benchConstraint.getNameRegex());
        this.descriptionPattern = Pattern.compile(benchConstraint.getDescriptionRegex());
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
            if(benchConstraint.isNameRequired())
                validationResult.addInvalidation(ValidationResult.InvalidMessage
                        .builder()
                        .type(String.class)
                        .field("name")
                        .message("name cannot be null")
                        .build());
        }else if(bench.getName().length()< benchConstraint.getNameMinLength() ||
                bench.getName().length()> benchConstraint.getNameMaxLength()) {
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message(String.format("name length must be between %d and %d",
                            benchConstraint.getNameMinLength(), benchConstraint.getNameMaxLength()))
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
            if(benchConstraint.isDescriptionRequired())
                validationResult.addInvalidation(ValidationResult.InvalidMessage
                        .builder()
                        .type(String.class)
                        .field("description")
                        .message("description cannot be null")
                        .build());
        }else if(bench.getDescription().length()< benchConstraint.getDescriptionMinLength() ||
                bench.getDescription().length()> benchConstraint.getDescriptionMaxLength()) {
            validationResult.addInvalidation(ValidationResult.InvalidMessage
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message(String.format("description length must be between %d and %d",
                            benchConstraint.getDescriptionMinLength(), benchConstraint.getDescriptionMaxLength()))
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
//        if(bench.getExpireTime()< benchConstraint.getExpirationMinDuration().toMillis()){
//            validationResult.addInvalidation(ValidationResult.InvalidMessage
//                    .builder()
//                    .type(Long.class)
//                    .field("expiration")
//                    .message(String.format("expiration must be bigger than %d ms",
//                            benchConstraint.getExpirationMinDuration().toMillis()))
//                    .build());
//        }else if(bench.getExpireTime()> benchConstraint.getExpirationMaxDuration().toMillis()){
//            validationResult.addInvalidation(ValidationResult.InvalidMessage
//                    .builder()
//                    .type(Long.class)
//                    .field("expiration")
//                    .message(String.format("expiration must be smaller than %d ms",
//                            benchConstraint.getExpirationMaxDuration().toMillis()))
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
