package com.jerome.exceptions;

public class ProcessingEventException extends RuntimeException {
    public ProcessingEventException(Throwable e) {
        super(e);
    }
}
