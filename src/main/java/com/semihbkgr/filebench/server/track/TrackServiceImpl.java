package com.semihbkgr.filebench.server.track;

import org.springframework.stereotype.Service;

@Service
public class TrackServiceImpl implements TrackService {

    private final AllTrack allTrack;
    private final DailyTrack dailyTrack;

    public TrackServiceImpl() {
        this.allTrack = new AllTrack();
        this.dailyTrack = new DailyTrack();
    }

    @Override
    public void created() {
        created(1);
    }

    @Override
    public void created(int count) {
        this.allTrack.createdBenchCount.getAndAdd(count);
    }

    @Override
    public void upload() {
        upload(1);
    }

    @Override
    public void upload(int count) {
        this.allTrack.downloadedImageCount.getAndAdd(count);
    }

    @Override
    public void download() {
        download(1);
    }

    @Override
    public void download(int count) {
        this.allTrack.uploadedImageCount.getAndAdd(count);
    }

}
