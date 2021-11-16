package com.semihbkgr.filebench.server.track;

import java.util.concurrent.atomic.AtomicLong;

public class AllTrack implements Track {

    public final AtomicLong createdBenchCount;
    public final AtomicLong uploadedImageCount;
    public final AtomicLong downloadedImageCount;

    public AllTrack() {
        this.createdBenchCount = new AtomicLong(0L);
        this.uploadedImageCount = new AtomicLong(0L);
        this.downloadedImageCount = new AtomicLong(0L);
    }

    @Override
    public String getLogMessage() {
        return String.format("create: %d, upload: %d, download: %s",
                createdBenchCount.get(), uploadedImageCount.get(), downloadedImageCount.get());
    }

}
