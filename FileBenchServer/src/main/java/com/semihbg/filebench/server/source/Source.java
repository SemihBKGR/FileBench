package com.semihbg.filebench.server.source;

import com.semihbg.filebench.server.model.Bench;

import java.io.IOException;

public interface Source {

    void save(SourceContext sourceContext) throws IOException;

    void delete(Bench bench);

}
