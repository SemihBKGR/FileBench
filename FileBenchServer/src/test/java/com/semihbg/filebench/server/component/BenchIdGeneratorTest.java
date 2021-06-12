package com.semihbg.filebench.server.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BenchIdGeneratorTest {

    static final int ID_LENGTH=7;

    BenchIdGenerator benchIdGenerator;
    long generateTime;

    @BeforeEach
    void initialize(){
        benchIdGenerator=new BenchIdGenerator(ID_LENGTH);
        generateTime=100;
    }

    @Test
    @DisplayName("Generates lost of id and ensure that they are correct format")
    void generate() {
        for(int i=1;i<=generateTime;i++){
            String id= benchIdGenerator.generate();
            log.info("{}.Id Generated, BenchId : {}",i,id);
            assertEquals(ID_LENGTH,id.length(),()->"Generated id's length is not same to desired length");
            try{
                Long.parseUnsignedLong(id);
            }catch (Exception ignore){
                Assertions.fail(String.format("Non-Numerical id generation, id = %s",id));
            }
        }
    }

}