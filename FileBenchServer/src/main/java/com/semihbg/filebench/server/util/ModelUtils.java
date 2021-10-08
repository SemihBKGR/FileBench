package com.semihbg.filebench.server.util;

import com.semihbg.filebench.server.component.IdGenerator;
import com.semihbg.filebench.server.model.dto.BenchCreateDto;
import com.semihbg.filebench.server.model.dto.FileCreateDto;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModelUtils {

    public static Bench benchOf(@Nullable String id, @NonNull BenchCreateDto bcd) {
        return Bench
                .builder()
                .id(id)
                .name(bcd.getName())
                .description(bcd.getDescription())
                .files(allFilesOf(bcd.getFiles()))
                .createdTime(0)
                .expireTime(bcd.getExpireTime())
                .viewCount(0)
                .build();
    }

    public static Bench benchOf(@NonNull BenchCreateDto bcd) {
        return benchOf(null,bcd);
    }

    public static File fileOf(@Nullable String id,@NonNull FileCreateDto fcd) {
        return File
                .builder()
                .id(id)
                .name(fcd.getName())
                .path(fcd.getPath())
                .label(fcd.getLabel())
                .description(fcd.getDescription())
                .size(fcd.getSize())
                .downloadCount(0)
                .build();
    }

    public static File fileOf(@NonNull FileCreateDto fcd) {
        return fileOf(null,fcd);
    }

    public static List<File> allFilesOf(@NonNull IdGenerator<? extends String> idGenerator,
                                        @NonNull Iterable<FileCreateDto> fcdIterable) {
        return StreamSupport
                .stream(fcdIterable.spliterator(), false)
                .map(fcd-> fileOf(idGenerator.generate(),fcd))
                .collect(Collectors.toList());
    }

    public static List<File> allFilesOf(@NonNull Iterable<FileCreateDto> fcdIterable) {
        return StreamSupport
                .stream(fcdIterable.spliterator(), false)
                .map(ModelUtils::fileOf)
                .collect(Collectors.toList());
    }

}
