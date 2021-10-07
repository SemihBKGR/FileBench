package com.semihbg.filebench.server.util;

import com.semihbg.filebench.server.dto.BenchCreateDto;
import com.semihbg.filebench.server.dto.FileCreateDto;
import com.semihbg.filebench.server.model.Bench;
import com.semihbg.filebench.server.model.File;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModelUtils {

    public static Bench benchOf(@NonNull BenchCreateDto bcd) {
        return benchOf(null,bcd,0);
    }

    public static Bench benchOf(@Nullable String id, @NonNull BenchCreateDto bcd, long createdTime) {
        return Bench
                .builder()
                .id(id)
                .name(bcd.getName())
                .description(bcd.getDescription())
                .files(allFilesOf(bcd.getFiles()))
                .createdTime(createdTime)
                .expireTime(bcd.getExpireTime())
                .viewCount(0)
                .build();
    }

    public static File fileOf(@NonNull FileCreateDto fcd) {
        return File
                .builder()
                .name(fcd.getName())
                .path(fcd.getPath())
                .label(fcd.getLabel())
                .description(fcd.getDescription())
                .size(fcd.getSize())
                .downloadCount(0)
                .build();
    }

    public static List<File> allFilesOf(@NonNull Iterable<FileCreateDto> fcdIterable) {
        return StreamSupport
                .stream(fcdIterable.spliterator(), false)
                .map(ModelUtils::fileOf)
                .collect(Collectors.toList());
    }

}
