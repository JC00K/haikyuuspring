package com.example.haikyuuspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("No results found with id " + id);
    }
    public ResourceNotFoundException(String name) {
        super("No results found with " + name);
    }
    public ResourceNotFoundException(int id) {
        super("No results found with id " + id);
    }
}
