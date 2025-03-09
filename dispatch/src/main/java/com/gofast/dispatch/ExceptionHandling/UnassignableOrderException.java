package com.gofast.dispatch.ExceptionHandling;


public class UnassignableOrderException extends RuntimeException {
    public UnassignableOrderException(String message) {
        super(message);
    }
}