package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FileValidator implements Validator<File> {

    private final FileConstraints fileConstraints;
    private final Pattern namePattern;
    private final Pattern descriptionPattern;
    private final Pattern labelPattern;

    public FileValidator(FileConstraints fileConstraints) {
        log.info("FileValidator is started with '{}'", fileConstraints);
        this.fileConstraints = fileConstraints;
        this.namePattern = Pattern.compile(fileConstraints.getNameRegex());
        this.descriptionPattern = Pattern.compile(fileConstraints.getDescriptionRegex());
        this.labelPattern = Pattern.compile(fileConstraints.getLabelRegex());
    }

    @Override
    public Mono<ValidationResult> validate(File file) {
        if (file.getName() != null)
            file.setName(file.getName().isBlank() ? null : file.getName().strip());
        if (file.getDescription() != null)
            file.setDescription(file.getDescription().isBlank() ? null : file.getDescription().strip());
        if (file.getLabel() != null)
            file.setDescription(file.getDescription().isBlank() ? null : file.getDescription().strip());
        var validationResult = ValidationResult.empty();
        checkName(file.getName()).ifPresent(validationResult::addInvalidationUnit);
        checkDescription(file.getDescription()).ifPresent(validationResult::addInvalidationUnit);
        checkLabel(file.getLabel()).ifPresent(validationResult::addInvalidationUnit);
        return Mono.just(validationResult);
    }

    private Optional<ValidationResult.InvalidationUnit> checkName(String name) {
        ValidationResult.InvalidationUnit invalidationUnit = null;
        if (name == null) {
            if (fileConstraints.isNameRequired())
                invalidationUnit = ValidationResult.InvalidationUnit
                        .builder()
                        .type(String.class)
                        .field("name")
                        .message("name cannot be null and empty")
                        .build();
        } else if (name.length() < fileConstraints.getNameMinLength() ||
                name.length() > fileConstraints.getNameMaxLength()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("name")
                    .message(String.format("name length must be between %d - %d",
                            fileConstraints.getNameMinLength(), fileConstraints.getNameMaxLength()))
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
            if (fileConstraints.isDescriptionRequired())
                invalidationUnit = ValidationResult.InvalidationUnit
                        .builder()
                        .type(String.class)
                        .field("description")
                        .message("description cannot be null")
                        .build();
        } else if (description.length() < fileConstraints.getDescriptionMinLength() ||
                description.length() > fileConstraints.getDescriptionMaxLength()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("description")
                    .message(String.format("description length must be between %d and %d",
                            fileConstraints.getDescriptionMinLength(), fileConstraints.getDescriptionMaxLength()))
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

    private Optional<ValidationResult.InvalidationUnit> checkLabel(String label) {
        ValidationResult.InvalidationUnit invalidationUnit = null;
        if (label == null) {
            if (fileConstraints.isLabelRequired())
                invalidationUnit = ValidationResult.InvalidationUnit
                        .builder()
                        .type(String.class)
                        .field("label")
                        .message("label cannot be null")
                        .build();
        } else if (label.length() < fileConstraints.getLabelMinLength() ||
                label.length() > fileConstraints.getLabelMaxLength()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("label")
                    .message(String.format("label length must be between %d and %d",
                            fileConstraints.getLabelMinLength(), fileConstraints.getLabelMaxLength()))
                    .build();
        } else if (!labelPattern.matcher(label).matches()) {
            invalidationUnit = ValidationResult.InvalidationUnit
                    .builder()
                    .type(String.class)
                    .field("label")
                    .message("label contains illegal characters")
                    .build();
        }
        return Optional.ofNullable(invalidationUnit);
    }

}
