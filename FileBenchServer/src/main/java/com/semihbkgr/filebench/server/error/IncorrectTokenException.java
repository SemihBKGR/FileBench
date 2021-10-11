package com.semihbkgr.filebench.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectTokenException extends ResponseStatusException {

    public IncorrectTokenException(HttpStatus status) {
        super(status);
    }

    public IncorrectTokenException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public IncorrectTokenException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public IncorrectTokenException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

}
