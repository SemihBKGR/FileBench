package com.semihbkgr.filebench.server.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class NumericalIdGeneratorTest {

    static final int ID_LENGTH=7;
    static final String REGEX_PATTERN="^[0-9]+$";

    NumericalIdGenerator numericalIdGenerator;
    long generateTime;
    Pattern pattern;

    @BeforeEach
    void initialize(){
        numericalIdGenerator =new NumericalIdGenerator(ID_LENGTH);
        generateTime=100;
        pattern=Pattern.compile(REGEX_PATTERN);
    }

    @Test
    @DisplayName("Generates Ids and Check Format")
    void generate() {
        for(int i=1;i<=generateTime;i++){
            String id= numericalIdGenerator.generate();
            log.info("{}.Id Generated, BenchId : {}",i,id);
            assertEquals(ID_LENGTH,id.length(),()->"Generated id's length is not same to desired length");
            assertTrue(pattern.matcher(id).matches(),()->"Generated id's format is not as desired");
        }
    }

}