package com.semihbkgr.filebench.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class File implements Parcelable {

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

    protected File(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        label = in.readString();
        size = in.readLong();
        downloadCount = in.readLong();
    }

    public static final Creator<File> CREATOR = new Creator<File>() {
        @Override
        public File createFromParcel(Parcel in) {
            return new File(in);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(label);
        dest.writeLong(size);
        dest.writeLong(downloadCount);
    }

}
