package com.semihbkgr.filebench.android.model;

public class File {

    private String id;
    private String name;
    private String description;
    private String label;
    private long size;
    private long downloadCount;

    public File() {
    }

    public File(String id, String name, String description, String label, long size, long downloadCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.label = label;
        this.size = size;
        this.downloadCount = downloadCount;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }

}
