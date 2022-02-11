package com.semihbkgr.filebench.server.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class LocalStorageServiceTest {

    static final String ROOT_DIR = "test_root_dir";
    static final String DIR_NAME = "test_dir";
    static final String FILE_NAME = "test_file.txt";

    LocalStorageService storageService;

    @AfterAll
    static void clearEnv() throws IOException {
        Files.walkFileTree(Path.of(ROOT_DIR), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @BeforeEach
    void createEnv() throws IOException {
        this.storageService = new LocalStorageService(ROOT_DIR);
        storageService.createOrClearRootDir();
        var testFileInputStream = new ClassPathResource(DIR_NAME + '/' + FILE_NAME).getInputStream();
        Files.createDirectories(Path.of(ROOT_DIR).resolve(DIR_NAME));
        Files.write(Path.of(ROOT_DIR).resolve(DIR_NAME).resolve(FILE_NAME), testFileInputStream.readAllBytes(), StandardOpenOption.CREATE);
    }

    @Test
    @DisplayName("Save mocked FilePart")
    void saveFile() {
        var mockedFilePart = Mockito.mock(FilePart.class);
        Mockito.when(mockedFilePart.filename()).thenReturn("testfile");
        Mockito.when(mockedFilePart.transferTo((Path) ArgumentMatchers.any())).thenReturn(Mono.empty());
        Mockito.when(mockedFilePart.transferTo((File) ArgumentMatchers.any())).thenReturn(Mono.empty());
        var mono = storageService.saveFile(DIR_NAME, FILE_NAME, mockedFilePart).log();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    @DisplayName("Get file does not exist")
    void getExistingFile() {
        var mono = storageService.getFile(DIR_NAME, FILE_NAME).log();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    @DisplayName("Get file does not exist")
    void getNonExistingFile() {
        var mono = storageService.getFile(DIR_NAME, "non-existing-test-file").log();
        StepVerifier.create(mono).verifyError(NoSuchFileException.class);
    }

    @Test
    @DisplayName("Delete existing file")
    void deleteExistingFile() {
        var mono = storageService.deleteFile(DIR_NAME, FILE_NAME).log();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    @DisplayName("Delete file does not exist")
    void deleteNonExistingFile() {
        var mono = storageService.deleteFile(DIR_NAME, "non-existing-test-file").log();
        StepVerifier.create(mono).verifyError(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Delete existing bench")
    void deleteExistingBench() {
        var mono = storageService.deleteBench(DIR_NAME).log();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    @DisplayName("Delete bench does not exist")
    void deleteNonExistingBench() {
        var mono = storageService.deleteBench("non-existing-test-bench").log();
        StepVerifier.create(mono).verifyError(IllegalArgumentException.class);
    }

}