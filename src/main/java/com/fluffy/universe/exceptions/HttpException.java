package com.fluffy.universe.exceptions;

public class HttpException extends RuntimeException {
    private final int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HttpException(int code) {
        this.code = code;
    }
}
