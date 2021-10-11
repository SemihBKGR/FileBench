package com.semihbkgr.filebench.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Bench implements Parcelable {

    public static final Creator<Bench> CREATOR = new Creator<Bench>() {
        @Override
        public Bench createFromParcel(Parcel in) {
            return new Bench(in);
        }

        @Override
        public Bench[] newArray(int size) {
            return new Bench[size];
        }
    };
    private String id;
    private String token;
    private String name;
    private String description;
    private List<File> files;
    private long creationTimeMs;
    private long expirationTimeMs;
    private long viewCount;

    public Bench() {
    }

    public Bench(String id, String token, String name, String description, List<File> files, long creationTimeMs, long expirationTimeMs, long viewCount) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.description = description;
        this.files = files;
        this.creationTimeMs = creationTimeMs;
        this.expirationTimeMs = expirationTimeMs;
        this.viewCount = viewCount;
    }

    protected Bench(Parcel in) {
        id = in.readString();
        token = in.readString();
        name = in.readString();
        description = in.readString();
        creationTimeMs = in.readLong();
        expirationTimeMs = in.readLong();
        viewCount = in.readLong();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(token);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(creationTimeMs);
        dest.writeLong(expirationTimeMs);
        dest.writeLong(viewCount);
    }

}
