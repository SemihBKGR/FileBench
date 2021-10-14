package com.semihbkgr.filebench.server.component;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageScaleComponentImpl implements ImageScaleComponent {

    @Override
    public Flux<DataBuffer> scale(Flux<DataBuffer> content, String extension, int width, int height) {
        return content
                .collectList()
                .map(dataBuffers -> {
                    var baOutputStream = new ByteArrayOutputStream();
                    for (var dataBuffer : dataBuffers) {
                        var bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        try {
                            baOutputStream.write(bytes);
                        } catch (IOException e) {
                            throw new ImageScaleException(e);
                        }
                    }
                    return baOutputStream.toByteArray();
                })
                .map(bytes -> {
                    try {
                        var image = ImageIO.read(new ByteArrayInputStream(bytes))
                                .getScaledInstance(width, height, Image.SCALE_DEFAULT);
                        var scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        scaledImage.getGraphics().drawImage(image, 0, 0, null);
                        return scaledImage;
                    } catch (IOException e) {
                        throw new ImageScaleException(e);
                    }
                })
                .flatMapMany(scaledImage -> {
                    return DataBufferUtils.readInputStream(() -> {
                        var baOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(scaledImage, extension, baOutputStream);
                        return new ByteArrayInputStream(baOutputStream.toByteArray());
                    }, new DefaultDataBufferFactory(), StreamUtils.BUFFER_SIZE);
                });
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
