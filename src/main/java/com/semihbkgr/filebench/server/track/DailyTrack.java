package com.semihbkgr.filebench.server.track;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyTrack implements Track {

    public final AtomicInteger createdBenchCount;
    public final AtomicInteger uploadedImageCount;
    public final AtomicInteger downloadedImageCount;
    private final Date date;

    public DailyTrack() {
        this.date = new Date();
        this.createdBenchCount = new AtomicInteger(0);
        this.uploadedImageCount = new AtomicInteger(0);
        this.downloadedImageCount = new AtomicInteger(0);
    }

    @Override
    public String getLogMessage() {
        return String.format("Date: %s, create: %d, upload: %d, download: %s",
                date.toString(), createdBenchCount.get(), uploadedImageCount.get(), downloadedImageCount.get());
    }

}
