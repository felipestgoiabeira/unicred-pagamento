package com.unicred.exception;

public class InvalidOperationExecption extends Exception {

    public InvalidOperationExecption(String s) {
        super(s);
    }

    public InvalidOperationExecption() {
        super("Operação inválida na aplicação!");
    }

    public InvalidOperationExecption(String s, Throwable throwable) {
        super(s, throwable);
    }
}
