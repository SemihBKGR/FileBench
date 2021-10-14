package com.semihbkgr.filebench.server.component;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class ImageScaleComponentImpl implements ImageScaleComponent {


    @Override
    public Mono<Void> scale(Path tar, Path dest, String ext, int maxWidth, int maxHeight) {
        return null;
    }

    public static class ImageScaleException extends ResponseStatusException {

        public ImageScaleException(Throwable cause) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, "Error while scaling image", cause);
        }

        public ImageScaleException(String reason, Throwable cause) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
        }

    }

}
