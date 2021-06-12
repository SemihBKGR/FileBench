package com.semihbg.filebench.server.file;

import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import lombok.*;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileContext {

    private Bench bench;
    private Flux<FilePart> filePartFlux;

    //Linear search
    //Throws error if file not found
    public File findFileByName(@NonNull String fileName){
        for (File file:bench.getFiles())
            if(fileName.equals(file.getName()))
                return file;
        throw new IllegalArgumentException("There is no file found in bench");
    }

}
