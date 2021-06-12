package com.semihbg.filebench.server.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class FileIdGeneratorTest {

    static final int ID_LENGTH=25;
    static final String REGEX_PATTERN="[A-Za-z0-9]+";

    FileIdGenerator fileIdGenerator;
    long generateTime;
    Pattern pattern;

    @BeforeEach
    void initialize(){
        fileIdGenerator=new FileIdGenerator(ID_LENGTH);
        generateTime=100;
        pattern=Pattern.compile(REGEX_PATTERN);
    }

    @Test
    @DisplayName("Generates Ids and Check Format")
    void generate() {
        for(int i=1;i<=generateTime;i++){
            String id= fileIdGenerator.generate();
            log.info("{}.Id Generated, FileId : {}",i,id);
            assertEquals(ID_LENGTH,id.length(),()->"Generated id's length is not same to desired length");
            assertTrue(pattern.matcher(id).matches(),()->"Generated id's format is not as desired");
        }
    }

}