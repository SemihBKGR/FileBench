package com.smh.bs.server.exception;

import java.io.IOException;

public class ResourceStorageException extends IOException {

    public ResourceStorageException() { }

    public ResourceStorageException(String message) {
        super(message);
    }

}
