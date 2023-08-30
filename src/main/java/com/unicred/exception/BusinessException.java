package com.unicred.exception;

public class BusinessException extends Exception {

    public BusinessException(String s) {
        super(s);
    }

    public BusinessException() {
        super("Ocorreu um erro inesperado na aplicação!");
    }

    public BusinessException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
