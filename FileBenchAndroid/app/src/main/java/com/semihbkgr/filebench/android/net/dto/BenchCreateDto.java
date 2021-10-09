package com.semihbkgr.filebench.android.net.dto;

public class BenchCreateDto {

    private String name;
    private String description;
    private long expirationDurationMs;

    public BenchCreateDto() {
    }

    public BenchCreateDto(String name, String description, long expirationDurationMs) {
        this.name = name;
        this.description = description;
        this.expirationDurationMs = expirationDurationMs;
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

    public long getExpirationDurationMs() {
        return expirationDurationMs;
    }

    public void setExpirationDurationMs(long expirationDurationMs) {
        this.expirationDurationMs = expirationDurationMs;
    }

}
