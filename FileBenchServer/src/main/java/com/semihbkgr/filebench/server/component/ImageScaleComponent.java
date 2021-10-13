package com.semihbkgr.filebench.server.component;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface ImageScaleComponent {

    Flux<DataBuffer> scale(Flux<DataBuffer> content,String extension,int width,int height);

}
