package com.example.haikyuuspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceDuplicateException extends RuntimeException {
    public ResourceDuplicateException(String value) {
        super(value + " already exists");
    }
}
