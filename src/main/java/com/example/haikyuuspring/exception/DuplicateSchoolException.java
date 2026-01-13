package com.example.haikyuuspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateSchoolException extends RuntimeException{
    public DuplicateSchoolException(String school) {
        super("School " + school + " already exists");
    }
}
