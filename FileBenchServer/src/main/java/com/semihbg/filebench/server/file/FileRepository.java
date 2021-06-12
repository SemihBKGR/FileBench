package com.semihbg.filebench.server.file;

import com.semihbg.filebench.server.model.Bench;

import java.io.IOException;

public interface FileRepository {

    void save(FileContext fileContext) throws IOException;

    void delete(Bench bench);

}
