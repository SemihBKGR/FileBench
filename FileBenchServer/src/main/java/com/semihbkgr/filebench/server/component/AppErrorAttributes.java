package com.semihbkgr.filebench.server.component;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        var map = new HashMap<String, Object>();
        map.put("timestamp", System.currentTimeMillis());
        var exception = getError(request);
        if (exception instanceof HttpStatusCodeException) {
            var httpStatusCodeException = (HttpStatusCodeException) exception;
            map.put("status", httpStatusCodeException.getRawStatusCode());
            map.put("error", httpStatusCodeException.getStatusCode().name());
        } else if (exception instanceof ResponseStatusException) {
            var responseStatusException = (ResponseStatusException) exception;
            map.put("status", responseStatusException.getRawStatusCode());
            map.put("error", responseStatusException.getStatus().name());
        } else {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("error", HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        map.put("exception", exception.getClass().getName());
        map.put("message", exception.getMessage());
        return map;
    }

}
