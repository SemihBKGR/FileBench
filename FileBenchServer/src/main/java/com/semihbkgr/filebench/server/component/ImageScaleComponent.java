package com.semihbkgr.filebench.server.component;

import reactor.core.publisher.Mono;

import java.nio.file.Path;

public interface ImageScaleComponent {

    Mono<Void> scale(Path tar,Path dest, String ext,int maxWidth, int maxHeight);

}
