package com.semihbg.filebench.server.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BenchIdGeneratorTest {

    static final int ID_LENGTH=7;

    BenchIdGenerator benchIdGenerator;

    @BeforeEach
    void initialize(){
        benchIdGenerator=new BenchIdGenerator(ID_LENGTH);
    }

    @Test
    void generate() {
        for(int i=0;i<10;i++){
            String id= benchIdGenerator.generate();
            log.info("BenchId : {}",id);
            try{
                Long.parseUnsignedLong(id);
            }catch (Exception ignore){
                Assertions.fail(String.format("Non-Numerical id generation, id = %s",id));
            }
        }
    }

}