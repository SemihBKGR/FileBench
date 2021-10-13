package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.model.Bench;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
public class BenchValidator implements Validator<Bench> {

    private final BenchConstraints benchConstraints;
    private final Pattern namePattern;
    private final Pattern descriptionPattern;

    public BenchValidator(BenchConstraints benchConstraints) {
        log.info("BenchValidator is started with '{}'", benchConstraints);
        this.benchConstraints = benchConstraints;
        this.namePattern = Pattern.compile(benchConstraints.getNameRegex());
        this.descriptionPattern = Pattern.compile(benchConstraints.getDescriptionRegex());
    }

    @Override
    public Mono<ValidationResult> validate(Bench bench) {
        bench.setName(bench.getName().isBlank() ? null : bench.getName().strip());
        bench.setDescription(bench.getDescription().isBlank() ? null : bench.getDescription().strip());
        var validationResult = ValidationResult.empty();
        checkName(bench.getName()).ifPresent(validationResult::addInvalidationUnit);
        checkDescription(bench.getDescription()).ifPresent(validationResult::addInvalidationUnit);
        checkExpirationDuration(bench.getExpirationDurationMs()).ifPresent(validationResult::addInvalidationUnit);
        return Mono.just(validationResult);
    }

    private Optional<ValidationResult.InvalidationUnit> checkName(String name) {
        ValidationResult.InvalidationUnit invalidationUnit = null;
        if (name == null) {
            if (benchConstraints.isNameRequired())
                invalidationUnit = ValidationResult.InvalidationUnit
                        .builder()
                        .type(String.class)
                        .field("name")
                        .message("name cannot be null and empty")
                        .build();
        } else if (name.length() < benchConstraints.getNameMinLength() ||
                name.length() > benchConstraints.getNameMaxLength()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message(String.format("name length must be between %d - %d",
                            benchConstraints.getNameMinLength(), benchConstraints.getNameMaxLength()))
                    .build();
        } else if (!namePattern.matcher(name).matches()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message("name contains illegal characters")
                    .build();
        }
        return Optional.ofNullable(invalidationUnit);
    }

    private Optional<ValidationResult.InvalidationUnit> checkDescription(String description) {
        ValidationResult.InvalidationUnit invalidationUnit = null;
        if (description == null) {
            if (benchConstraints.isDescriptionRequired())
                invalidationUnit = ValidationResult.InvalidationUnit
                        .builder()
                        .type(String.class)
                        .field("description")
                        .message("description cannot be null")
                        .build();
        } else if (description.length() < benchConstraints.getDescriptionMinLength() ||
                description.length() > benchConstraints.getDescriptionMaxLength()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message(String.format("description length must be between %d and %d",
                            benchConstraints.getDescriptionMinLength(), benchConstraints.getDescriptionMaxLength()))
                    .build();
        } else if (!descriptionPattern.matcher(description).matches()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message("description contains illegal characters")
                    .build();
        }
        return Optional.ofNullable(invalidationUnit);
    }

    private Optional<ValidationResult.InvalidationUnit> checkExpirationDuration(long expirationDurationMs) {
        ValidationResult.InvalidationUnit invalidationUnit = null;
        if (expirationDurationMs < benchConstraints.getExpirationMinDuration().toMillis()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(Long.class)
                    .field("expirationDurationMs")
                    .message(String.format("expiration duration ms value must be bigger than %d ms",
                            benchConstraints.getExpirationMinDuration().toMillis()))
                    .build();
        } else if (expirationDurationMs > benchConstraints.getExpirationMaxDuration().toMillis()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(Long.class)
                    .field("expirationDurationMs")
                    .message(String.format("expiration duration ms value must be smaller than %d ms",
                            benchConstraints.getExpirationMaxDuration().toMillis()))
                    .build();
        }
        return Optional.ofNullable(invalidationUnit);
    }


}
