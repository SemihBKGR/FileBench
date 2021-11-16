package com.semihbkgr.filebench.server.track;

public interface TrackService {

    void created();

    void created(int count);

    void upload();

    void upload(int count);

    void download();

    void download(int count);

}
