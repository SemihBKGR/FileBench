package com.semihbg.filebench.server.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.codec.multipart.FilePart;

import static org.junit.jupiter.api.Assertions.*;

class IOFileRepositoryTest {

    static final String FILE_PATH="files";

    IOFileRepository ioFileRepository;

    @BeforeEach
    void initialize(){
        ioFileRepository=new IOFileRepository(FILE_PATH);
    }



}