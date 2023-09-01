package com.unicred.exception;

public class ExpectationFailedException extends Exception {

    public ExpectationFailedException(String s) {
        super(s);
    }

    public ExpectationFailedException() {
        super("Ocorreu um erro inesperado na aplicação!");
    }

    public ExpectationFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
