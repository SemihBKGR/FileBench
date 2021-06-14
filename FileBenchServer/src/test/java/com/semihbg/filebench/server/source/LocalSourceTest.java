package com.semihbg.filebench.server.source;

import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocalSourceTest {

    static final String FILE_PATH="test-source";

    LocalFileSource localFileSource;

    @BeforeAll
    static void checkBefore(){
        if(Files.exists(Path.of(FILE_PATH))){
            throw new IllegalStateException("Test source directory is already exists, " +
                    "delete it and restart test");
        }
    }

    @AfterAll
    static void checkAfter(){
        if(Files.exists(Path.of(FILE_PATH))){
            throw new IllegalStateException("Test source directory is exists after test execution");
        }
    }

    @BeforeEach
    void initialize(){
        localFileSource =new LocalFileSource(FILE_PATH);
    }

    @Test
    @Order(0)
    @DisplayName("Create Root Source Directory")
    void createRootSourceDir(){
        localFileSource.createRootFolderIfNotExists();
    }

    @Test
    @Order(1)
    @DisplayName("Check Root Source Directory If Exists")
    void checkRootSourceDirIfExists(){
        Assertions.assertTrue(localFileSource.isRootDirectoryExists(),()->"Root Directory should be exists ");
    }

    @Test
    @Order(2)
    @DisplayName("Delete Root Source Directory")
    void deleteRootSourceDir(){
        localFileSource.deleteRootFolderIfNotExists();
    }

    @Test
    @Order(3)
    @DisplayName("Check Root Source Directory If Not Exists")
    void checkRootSourceDirIfNotExists(){
        Assertions.assertFalse(localFileSource.isRootDirectoryExists(),()->"Root Directory should be not exists ");
    }

}
