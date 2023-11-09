package com.anything.gradproject.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleArgumentException(Exception e) {
        return "<h1>" + e.getMessage() + "</h1>";
    }
}
