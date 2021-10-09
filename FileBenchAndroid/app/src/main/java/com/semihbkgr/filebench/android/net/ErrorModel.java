package com.semihbkgr.filebench.android.net;

public class ErrorModel {

    private long timestamp;
    private int status;
    private String exception;
    private String message;

    public ErrorModel() {
    }

    public ErrorModel(long timestamp, int status, String exception, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
