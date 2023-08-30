package com.unicred.exception;

public class EntityExistsException extends Exception{
    public EntityExistsException(String message) {
        super(message);
    }

    public EntityExistsException(Throwable t, String message) {
        super(message, t);
    }
}
