package com.semihbkgr.filebench.android.model;

import java.util.List;

public class Bench {

    private String id;
    private String name;
    private String description;
    private List<File> files;
    private long creationTimeMs;
    private long expirationTimeMs;
    private long viewCount;

    public Bench() {
    }

    public Bench(String id, String name, String description, List<File> files, long creationTimeMs, long expirationTimeMs, long viewCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.files = files;
        this.creationTimeMs = creationTimeMs;
        this.expirationTimeMs = expirationTimeMs;
        this.viewCount = viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public long getCreationTimeMs() {
        return creationTimeMs;
    }

    public void setCreationTimeMs(long creationTimeMs) {
        this.creationTimeMs = creationTimeMs;
    }

    public long getExpirationTimeMs() {
        return expirationTimeMs;
    }

    public void setExpirationTimeMs(long expirationTimeMs) {
        this.expirationTimeMs = expirationTimeMs;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

}
