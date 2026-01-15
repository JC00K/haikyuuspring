package com.example.haikyuuspring.controller.dto.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        String apiPath,
        HttpStatus httpStatus,
        String message,
        LocalDateTime timeStamp,
        List<String> errors
) {}
