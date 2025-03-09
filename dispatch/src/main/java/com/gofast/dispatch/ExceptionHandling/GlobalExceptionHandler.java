package com.gofast.dispatch.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<?> handleInvalidInputException(InvalidInputException e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage(), "status", "error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverCapacityException.class)
    public ResponseEntity<?> handleOverCapacityException(OverCapacityException e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage(), "status", "error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnassignableOrderException.class)
    public ResponseEntity<?> handleUnassignableOrderException(UnassignableOrderException e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage(), "status", "error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage(),"status", "error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}