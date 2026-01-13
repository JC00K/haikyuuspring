package com.example.haikyuuspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateCharacterException extends RuntimeException {
    public DuplicateCharacterException(String character) {
        super("Character " + character + " already exists");
    }
}
