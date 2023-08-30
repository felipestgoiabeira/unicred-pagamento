package com.unicred.exception;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable t, String message) {
        super(message, t);
    }
}
