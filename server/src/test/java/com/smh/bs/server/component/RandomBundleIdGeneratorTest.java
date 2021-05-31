package com.smh.bs.server.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBundleIdGeneratorTest {

    RandomBundleIdGenerator randomBundleIdGenerator;

    @BeforeEach
    void initialize(){
        randomBundleIdGenerator=new RandomBundleIdGenerator(7);
    }

    @Test
    void generate(){
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
        System.out.println(randomBundleIdGenerator.generate());
    }

}