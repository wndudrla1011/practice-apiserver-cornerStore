package com.rootable.apiserver.util;

public class CustomJWTException extends RuntimeException {

    public CustomJWTException(String message) {
        super(message);
    }

}