package com.example.haikyuuspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoPrefectureException extends RuntimeException {
    public NoPrefectureException(String prefecture) {
        super("Could not find any schools in " + prefecture);
    }
}
