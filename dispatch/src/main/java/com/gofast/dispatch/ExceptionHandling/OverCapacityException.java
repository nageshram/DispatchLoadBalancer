package com.gofast.dispatch.ExceptionHandling;

public class OverCapacityException extends RuntimeException {
    public OverCapacityException(String message) {
        super(message);
    }
}