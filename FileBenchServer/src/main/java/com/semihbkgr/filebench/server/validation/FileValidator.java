package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        this.fileConstraints=fileConstraints;
        this.namePattern = Pattern.compile(fileConstraints.getNameRegex());
        this.descriptionPattern = Pattern.compile(fileConstraints.getDescriptionRegex());
        this.labelPattern = Pattern.compile(fileConstraints.getLabelRegex());
    }

    @Override
    public ValidationResult validate(File file) {
        return null;
    }

}
