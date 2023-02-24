package com.fluffy.universe.exceptions;

public class CSRFTokenVerificationException extends RuntimeException {
    public CSRFTokenVerificationException(String message) {
        super(message);
    }
}
